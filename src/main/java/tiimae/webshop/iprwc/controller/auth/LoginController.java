package tiimae.webshop.iprwc.controller.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import tiimae.webshop.iprwc.DTO.LoginDTO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.token.TokenNotFoundException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.EncryptionService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

@RestController
public class LoginController extends AuthController {

    @PostMapping(value = ApiConstant.register)
    @ResponseBody
    public ApiResponseService register(@Valid @RequestBody UserDTO user, @RequestParam(required = false) boolean encrypted) throws InvalidDtoException, EntryAlreadyExistsException {
        this.userValidator.validateDTO(user);
        user.setVerified(false);
        user.setResetRequired(false);
        String password = encrypted ? EncryptionService.decryptAes(user.getPassword(), this.sharedSecret) : user.getPassword();

        String encodedPass = this.passwordEncoder.encode(password);

        user.setPassword(encodedPass);
        User newUser = userMapper.toUser(user);
        newUser.getRoles().add(this.roleRepository.findByName("User").get());

        this.userDAO.create(newUser);

        return new ApiResponseService<>(HttpStatus.ACCEPTED, "User has been registered");
    }

    @PostMapping(value = ApiConstant.login)
    @ResponseBody
    public ApiResponseService login(@RequestBody LoginDTO user, @RequestParam(required = false) boolean encrypted) throws EntryNotFoundException, InvalidDtoException, TokenNotFoundException {
        final HashMap<String, String> res = new HashMap<>();

        this.authValidator.loginValidation(user);

        if (encrypted) {
            user.setPassword(EncryptionService.decryptAes(user.getPassword(), sharedSecret));
        }

        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        try {
            this.authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            res.put("message", "You have entered an invalid email or password");
            return new ApiResponseService<HashMap<String, String>>(HttpStatus.UNAUTHORIZED, res);
        }

        User foundUser = this.userDAO.getByEmail(user.getEmail());

        this.loginService.generateTokens(foundUser);


        res.put("jwtToken", foundUser.getAccessToken().getValue());
        res.put("refreshToken", foundUser.getRefreshToken().getValue());
//        res.put("userId", String.valueOf(foundUser.getId()));
//        res.put("verified", foundUser.getVerified().toString());
        res.put("destination", "to-cookie");

        return new ApiResponseService<>(HttpStatus.OK, res);
    }

}
