package tiimae.webshop.iprwc.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.constants.ApiConstant;

@RestController
public class CsrfController {

    @GetMapping(value = ApiConstant.getCSRF)
    public CsrfToken csrfToken(CsrfToken token) {
        return token;
    }

}
