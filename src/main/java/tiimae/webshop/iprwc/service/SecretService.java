package tiimae.webshop.iprwc.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tiimae.webshop.iprwc.constants.ApiConstant;

@Service
public class SecretService {

    @Value("${jwt_secret}")
    private String jwtSecret;

    public String getSecret(HttpServletRequest request) {
        String secret = null;

        if (request.getCookies() != null) {
            secret = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("secret"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return secret;
    }

    public String createSecret() {
        String randomSecret = Date.from(Instant.now()).toString() + String.valueOf((new Random()).nextInt());
        return EncryptionService.getMd5(randomSecret);
    }

    public Cookie createCookie() {
        String secret = this.createSecret();
        secret = new EncryptionService().encrypt(secret, this.jwtSecret);
        Cookie cookie = new Cookie("secret", secret);

        cookie.setHttpOnly(true);
        cookie.setPath(ApiConstant.secret);
        //expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.set
        cookie.setDomain("localhost");
//        cookie.setDomain("timdekok.nl");
        return cookie;
    }

}
