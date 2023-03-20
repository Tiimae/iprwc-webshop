package tiimae.webshop.iprwc.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;
import tiimae.webshop.iprwc.service.auth.VerifyEmailService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.auth.VerifyEmailValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class VerifyController extends AuthController {

    private VerifyEmailService verifyEmailService;
    private VerifyEmailValidator verifyEmailValidator;

    @GetMapping(value = ApiConstant.sendVerifyEmail, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> sendVerifyEmail() throws EntryNotFoundException {
        Map<String, Object> res = new HashMap<>();

        Optional<User> bearerUser = this.profileService.getProfile(SecurityContextHolder.getContext().getAuthentication()).getPayload();

        final String s = this.verifyEmailValidator.validateIfUserAleadyHasBeenVerified(bearerUser);

        if (s != null) {
            res.put("message", s);
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        User user = bearerUser.get();

        this.emailService.setData(
                "Email verification timdekok.nl",
                user.getEmail(),
                "<p>Hi " + user.getFirstName() + ", this is de code to verify your email address. " + String.valueOf(this.verifyEmailService.generateVerifyUrl(user)) + "</p>");
        this.emailService.run();

        res.put("message", "Successfully sent a verify token to " + user.getEmail());
        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @PostMapping(value = ApiConstant.verifyEmail, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> verifyEmail(@RequestParam UUID token) throws EntryNotFoundException {
        Map<String, Object> res = new HashMap<>();

        Optional<User> bearerUser = this.profileService.getProfile(SecurityContextHolder.getContext().getAuthentication()).getPayload();
        Optional<VerifyToken> verifyToken = this.verifyTokenDAO.getToken(token);

        final String s = this.verifyEmailValidator.validateVerifyToken(bearerUser, verifyToken);

        if (s != null) {
            res.put("message", s);
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }


        UUID bearerUserId = bearerUser.get().getId();
        UUID tokenUserId = verifyToken.get().getUser().getId();

        if (bearerUserId.equals(tokenUserId)) {
            try {
                this.verifyTokenDAO.confirmToken(token);
                res.put("message", "Successfully confirmed your email. Redirecting you...");
                return new ApiResponseService<>(HttpStatus.OK, res);
            } catch (Exception e) {
                res.put("message", e.getMessage());
                return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
            }
        } else {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }
    }
}
