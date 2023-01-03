package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    @Transactional
    @Modifying
    @Query("delete from UserAddress t where t.id = ?1")
    void delete(UUID id);
}
