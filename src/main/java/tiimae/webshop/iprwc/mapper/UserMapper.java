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
import java.util.UUID;
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
        Set<Role> roles = this.getAllRoles(userDTO.getRoleIds());

        return new User(firstname, middleName, lastName, email, password, userDTO.getVerified(), userDTO.getResetRequired(), addresses, orders, roles);
    }

    public User mergeUser(User base, UserDTO update) {
        base.setFirstName(update.getFirstName());
        base.setMiddleName(update.getMiddleName());
        base.setLastName(update.getLastName());
        base.setEmail(update.getEmail());
        base.setPassword(update.getPassword());

        if (update.getRoleIds() != null) {
            for (Role role : this.getAllRoles(update.getRoleIds())) {
                if (!base.getRoles().contains(role)) {
                    base.getRoles().add(role);
                }
            }
        }

        return base;
    }

    public Set<Role> getAllRoles(UUID[] roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            roles = Arrays.stream(roleIds)
                    .map(id -> this.roleDAO.getRole(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return roles;
    }

}
