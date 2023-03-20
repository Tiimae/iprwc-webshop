package tiimae.webshop.iprwc.controller.auth;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

import java.security.Principal;
import java.util.Optional;

@RestController
public class OtherAuthController extends AuthController {

    @GetMapping(value = ApiConstant.profile, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Optional<User>> profile(Principal securityPrincipal) throws EntryNotFoundException {

        ApiResponseService<Optional<User>> profile = this.profileService.getProfile(securityPrincipal);

        profile.getPayload().get().getRoles().clear();

        return profile;
    }

}
