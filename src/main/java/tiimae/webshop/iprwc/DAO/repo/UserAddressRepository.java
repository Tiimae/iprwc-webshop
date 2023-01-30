package tiimae.webshop.iprwc.DAO.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tiimae.webshop.iprwc.models.UserAddress;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    @Transactional
    @Modifying
    @Query("delete from UserAddress t where t.id = ?1")
    void delete(UUID id);

    List<UserAddress> findAllByUserId(UUID userId);
}
