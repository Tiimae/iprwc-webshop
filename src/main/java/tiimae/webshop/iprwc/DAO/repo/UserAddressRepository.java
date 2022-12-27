package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

}
