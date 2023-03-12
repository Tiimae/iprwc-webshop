package tiimae.webshop.iprwc.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.constants.ApiConstant;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CsrfController {

    @GetMapping(value = ApiConstant.getCSRF)
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

}
