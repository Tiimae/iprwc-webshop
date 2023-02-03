package tiimae.webshop.iprwc.controller.auth;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.EncryptionService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

@RestController
public class OtherAuthController extends AuthController {

    @GetMapping(value = ApiConstant.toCookie, consumes = MediaType.ALL_VALUE)
    public ModelAndView redirectWithUsingForwardPrefix(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("attribute", "forwardWithForwardPrefix");
        response.addCookie(this.secretService.createCookie());
        return new ModelAndView("redirect:" + request.getHeader(HttpHeaders.REFERER) + "", model);
    }

    @GetMapping(value = ApiConstant.secret, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService secret(HttpServletRequest request) {
        String secret = this.secretService.getSecret(request);

        if (secret == null || secret.isBlank() || secret.isEmpty()) {
            return new ApiResponseService(HttpStatus.FORBIDDEN, "You are not authenticated");
        }

        secret = new EncryptionService().decrypt(secret, this.jwtSecret);

        return new ApiResponseService(HttpStatus.ACCEPTED, secret);
    }

    @GetMapping(value = ApiConstant.profile, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Optional<User>> profile(Principal securityPrincipal) {

        ApiResponseService<Optional<User>> profile = this.profileService.getProfile(securityPrincipal);

        profile.getPayload().get().getRoles().clear();

        return profile;
    }

}
