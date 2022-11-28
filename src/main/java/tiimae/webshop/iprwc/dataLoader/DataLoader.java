package tiimae.webshop.iprwc.dataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.RoleRepository;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;

import java.util.HashSet;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    private RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final List<Role> all = this.roleRepository.findAll();

        if (all.isEmpty()) {
            this.roleRepository.save(new Role("Owner", new HashSet<>()));
            this.roleRepository.save(new Role("Admin", new HashSet<>()));
            this.roleRepository.save(new Role("User", new HashSet<>()));
        }
    }
}
