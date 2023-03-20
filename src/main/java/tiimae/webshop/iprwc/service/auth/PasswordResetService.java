package tiimae.webshop.iprwc.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tiimae.webshop.iprwc.DAO.VerifyTokenDAO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.VerifyTokenEnum;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;
import tiimae.webshop.iprwc.service.EncryptionService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private VerifyTokenDAO verifyTokenDAO;

    @Value("${shared_secret}")
    private String sharedSecret;
    @Value("${frontendUrl}")
    private String url;

    public UserDTO toUserDTOFromPasswordReset(User user, String password, Boolean encrypted) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setVerified(user.getVerified());
        userDTO.setResetRequired(user.getReset_required());

        String encodedPass = this.passwordEncoder.encode(
                encrypted
                        ? EncryptionService.decryptAes(password, this.sharedSecret)
                        : password
        );
        userDTO.setPassword(encodedPass);

        return userDTO;
    }

    public String generatePasswordResetUrl(User user) {
        UUID token = UUID.randomUUID();
        VerifyToken verifyToken = new VerifyToken(token, VerifyTokenEnum.PASSWORD.toString(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), null, user);
        this.verifyTokenDAO.saveVerifyToken(verifyToken);

        return this.url + "/auth/set-new-password?token=" + token;
    }

}
