package tiimae.webshop.iprwc.DAO.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tiimae.webshop.iprwc.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
   Optional<Token> findByValue(String value);
    Boolean existsByValue(String value);

    Boolean existsByUserId(UUID id);

    void deleteByUserId(UUID id);

    List<Token> findAllByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.id = ?1")
    void deleteById(UUID id);
}
