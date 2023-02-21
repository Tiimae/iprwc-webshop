package tiimae.webshop.iprwc.validators;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Role;


@AllArgsConstructor
@Component
public class EndpointValidator {

    private UserDAO userDAO;

    public Boolean ensureUserAccessWithOpenEndpoint(String userid, String email) throws AccessDeniedException, EntryNotFoundException {
        UUID id = UUID.fromString(userid);

        tiimae.webshop.iprwc.models.User user = this.userDAO.getByEmail(email);
        UUID actualId = user.getId();

        Set<Role> roles = user.getRoles();
        ArrayList<String> roleCodeNames = new ArrayList<>();


        for (Role role : roles) {
            roleCodeNames.add("ROLE_" + role.getName());
        }

        if (Objects.equals(id.toString(), actualId.toString()) || roleCodeNames.contains(RoleEnum.Admin.CODENAME)) {
            return true;
        } else {
            throw new AccessDeniedException("Access denied");
        }


    }
}
