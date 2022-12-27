package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.UserAddressRepository;

@Component
public class UserAddressDAO {

    private UserAddressRepository userAddressRepository;

    public UserAddressDAO(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }
}
