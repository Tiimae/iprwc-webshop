package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    public User toUser(UserDTO userDTO) {
        String firstname = userDTO.getFirstName();
        String middleName = userDTO.getMiddleName();
        String lastName = userDTO.getLastName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        Set<UserAddress> addresses = new HashSet<>();
        Set<Order> orders = new HashSet<>();
        Set<Role> roles = new HashSet<>();

        return new User(firstname, middleName, lastName, email, password, addresses, orders, roles);
    }

}
