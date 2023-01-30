package tiimae.webshop.iprwc.DAO.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiimae.webshop.iprwc.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
   Optional<Token> findByValue(String value);
    Boolean existsByValue(String value);

    Boolean existsByUserId(UUID id);

    void deleteByUserId(UUID id);
}
