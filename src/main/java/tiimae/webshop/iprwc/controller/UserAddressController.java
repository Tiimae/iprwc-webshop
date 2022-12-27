package tiimae.webshop.iprwc.controller;

import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;

@RestController
public class UserAddressController {
    private UserAddressDAO userAddressDAO;

    public UserAddressController(UserAddressDAO userAddressDAO) {
        this.userAddressDAO = userAddressDAO;
    }
}
