package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import tiimae.webshop.iprwc.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Modifying
    @Query("UPDATE User u SET " +
            "u.firstName = :firstname, " +
            "u.middleName = :middleName, " +
            "u.lastName = :lastName, " +
            "u.email = :email " +
            "WHERE u.id = :id"
    )
    void updateUser(String firstname, String middleName, String lastName, String email, UUID id);
    Optional<User> findByEmail(String email);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.verified = TRUE WHERE u.id = ?1")
    int verifyUser(UUID userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.reset_required = FALSE WHERE u.id = ?1")
    int resetUser(UUID userId);
}
