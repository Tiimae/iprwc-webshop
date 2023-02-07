package tiimae.webshop.iprwc.service.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tiimae.webshop.iprwc.DAO.VerifyTokenDAO;
import tiimae.webshop.iprwc.constants.VerifyTokenEnum;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;

@Service
public class VerifyEmailService {
    @Autowired
    private VerifyTokenDAO verifyTokenDAO;

    @Value("${frontendUrl}")
    private String url;

    public String generateVerifyUrl(User user) {
        UUID token = UUID.randomUUID();
        VerifyToken verifyToken = new VerifyToken(token, VerifyTokenEnum.VERIFY.toString(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), null, user);
        this.verifyTokenDAO.saveVerifyToken(verifyToken);

        return this.url + "/auth/verify?token=" + token;
    }
}
