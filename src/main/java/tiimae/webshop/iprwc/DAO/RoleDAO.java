package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.RoleRepository;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;

import java.util.Optional;
import java.util.UUID;

@Component
public class RoleDAO {

    private RoleRepository roleRepository;

    public RoleDAO(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getRole(UUID roleId) {
    return this.roleRepository.findById(roleId);
}
}
