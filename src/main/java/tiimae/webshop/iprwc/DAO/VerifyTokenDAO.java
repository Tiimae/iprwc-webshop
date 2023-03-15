package tiimae.webshop.iprwc.DAO;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.VerifyTokenRepository;
import tiimae.webshop.iprwc.constants.VerifyTokenEnum;
import tiimae.webshop.iprwc.models.VerifyToken;

@Component
@AllArgsConstructor
public class VerifyTokenDAO {
    private final VerifyTokenRepository verifyTokenRepository;
    private UserDAO userDAO;

    public void saveVerifyToken(VerifyToken token){
        this.verifyTokenRepository.save(token);
    }

    public Optional<VerifyToken> getToken(UUID token) {
        return verifyTokenRepository.findByToken(token);
    }

    public Optional<VerifyToken> getTokenByUserId(UUID userId) {
        return verifyTokenRepository.getVerifyTokenByUserId(userId);
    }

    public int setConfirmedAt(UUID token) {
        return verifyTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    @Transactional
    public void confirmToken(UUID token) {
        VerifyToken verifyToken = this.getToken(token).orElseThrow(() -> new IllegalStateException("This token is invalid"));

        if (verifyToken.getConfirmedAt() != null) {
            throw new IllegalStateException("This token is invalid");
        }

        LocalDateTime expiresAt = verifyToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This token has expired");
        }

        this.setConfirmedAt(token);

        if(verifyToken.getType().equals(VerifyTokenEnum.VERIFY.toString())){
            this.userDAO.verifyUser(verifyToken.getUser().getId());
        } else if(verifyToken.getType().equals(VerifyTokenEnum.PASSWORD.toString())) {
            this.userDAO.resetUser(verifyToken.getUser().getId());
        }

    }
}
