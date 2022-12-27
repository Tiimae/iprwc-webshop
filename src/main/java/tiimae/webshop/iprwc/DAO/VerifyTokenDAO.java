package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tiimae.webshop.iprwc.DAO.repo.VerifyTokenRepository;
import tiimae.webshop.iprwc.models.VerifyToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class VerifyTokenDAO {
    private final VerifyTokenRepository verifyTokenRepository;
    private UserDAO userDAO;

    public VerifyTokenDAO(VerifyTokenRepository verifyTokenRepository, UserDAO userDAO) {
        this.verifyTokenRepository = verifyTokenRepository;
        this.userDAO = userDAO;
    }

    public void saveVerifyToken(VerifyToken token){
        this.verifyTokenRepository.save(token);
    }

    public Optional<VerifyToken> getToken(UUID token) {
        return verifyTokenRepository.findByToken(token);
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

        if(verifyToken.getType().equals("email")){
            this.userDAO.verifyUser(verifyToken.getUser().getId());
        } else if(verifyToken.getType().equals("password")) {
            this.userDAO.resetUser(verifyToken.getUser().getId());
        }

    }
}
