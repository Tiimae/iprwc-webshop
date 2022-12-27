package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface VerifyTokenRepository extends JpaRepository<VerifyToken, UUID> {
    Optional<VerifyToken> findByToken(UUID token);

    @Transactional
    @Modifying
    @Query("UPDATE VerifyToken c SET c.confirmedAt = ?2 WHERE c.token = ?1")
    int updateConfirmedAt(UUID token, LocalDateTime confirmedAt);

    Optional<VerifyToken> getVerifyTokenByUser(User user);
}
