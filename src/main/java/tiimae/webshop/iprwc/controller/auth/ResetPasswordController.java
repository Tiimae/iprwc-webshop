package tiimae.webshop.iprwc.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.VerifyTokenEnum;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;
import tiimae.webshop.iprwc.service.auth.PasswordResetService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class ResetPasswordController extends AuthController {

    private PasswordResetService passwordResetService;

    @PostMapping(value = ApiConstant.forgotPassword, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> forgotPassword(@RequestParam String email) throws EntryNotFoundException {
        Map<String, Object> res = new HashMap<>();

        User user = this.userDAO.getByEmail(email);

        // Create and save Token in DB
        final String url = this.passwordResetService.generatePasswordResetUrl(user);

        this.emailService.setData(
                "Change timdekok.nl password",
                user.getEmail(),
                "<p>Hi " + user.getFirstName() + ", you notified us that you want to change your password. Use this link to change your password: <a href=" + url + ">Set new password</a></p>"
        );
        this.emailService.start();

        res.put("message", "Successfully sent a verify token to " + user.getEmail());
        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @PostMapping(value = ApiConstant.setNewPassword, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Map<String, Object>> setNewPassword(@RequestBody UserDTO newUser, @RequestParam UUID token, @RequestParam(required = false) boolean encrypted) throws EntryNotFoundException, EntryAlreadyExistsException, InvalidDtoException {
        this.authValidator.resetPasswordValidation(newUser);

        Map<String, Object> res = new HashMap<>();

        Optional<VerifyToken> verifyToken = this.verifyTokenDAO.getToken(token);
        User foundUser = this.userDAO.getByEmail(newUser.getEmail());

        if (!verifyToken.isPresent() || !verifyToken.get().getType().equals(VerifyTokenEnum.PASSWORD.toString())) {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        User tokenUser = this.userDAO.getUser(verifyToken.get().getUser().getId());

        UUID foundUserId = foundUser.getId();
        UUID tokenUserId = verifyToken.get().getUser().getId();

        if (!foundUserId.equals(tokenUserId)) {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        User user = tokenUser;

        final UserDTO userDTO = this.passwordResetService.toUserDTOFromPasswordReset(user, newUser.getPassword(), encrypted);;
        this.userDAO.update(user.getId(), userDTO);


        this.emailService.setData(
                "your password on timdekok.nl has been changed",
                user.getEmail(),
                "<p>Hi " + user.getFirstName() + ", Your password has been changed! If you didn't change password send a message to us.</p>"
        );
        this.emailService.run();

        try {
            this.verifyTokenDAO.confirmToken(token);
            res.put("message", "Successfully reset your password. Redirecting you...");
            return new ApiResponseService<>(HttpStatus.OK, res);
        } catch (Exception e) {
            res.put("message", e.getMessage());
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

    }

}
