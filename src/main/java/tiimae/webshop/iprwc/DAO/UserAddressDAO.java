package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.UserAddressRepository;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.List;

@Component
public class UserAddressDAO {

    private UserAddressRepository userAddressRepository;

    public UserAddressDAO(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    public List<UserAddress> getAll() {
        return this.userAddressRepository.findAll();
    }
}
