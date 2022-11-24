package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.RoleDAO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private RoleDAO roleDAO;

    public UserMapper(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public User toUser(UserDTO userDTO) {
        String firstname = userDTO.getFirstName();
        String middleName = userDTO.getMiddleName();
        String lastName = userDTO.getLastName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        Set<UserAddress> addresses = new HashSet<>();
        Set<Order> orders = new HashSet<>();
        Set<Role> roles = new HashSet<>();

        if (userDTO.getRoleIds() != null) {
            roles = Arrays.stream(userDTO.getRoleIds())
                    .map(id -> this.roleDAO.getRole(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return new User(firstname, middleName, lastName, email, password, addresses, orders, roles);
    }

}
