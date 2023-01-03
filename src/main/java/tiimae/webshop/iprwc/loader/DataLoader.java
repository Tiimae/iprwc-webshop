package tiimae.webshop.iprwc.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user_pw}")
    private String userPW;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final List<Role> all = this.roleRepository.findAll();

        if (all.isEmpty()) {
            final Role admin = new Role("Admin", new HashSet<>());
            final Role owner = new Role("Owner", new HashSet<>());
            final Role user = new Role("User", new HashSet<>());

            this.roleRepository.save(owner);
            this.roleRepository.save(admin);
            this.roleRepository.save(user);


            final HashSet<Role> objects = new HashSet<>();
            objects.add(admin);
            objects.add(owner);
            objects.add(user);
            final String test123 = passwordEncoder.encode(this.userPW);

            this.userRepository.save(new User(
                    "Tim",
                    "de",
                    "Kok",
                    "de.kok.ac@gmail.com",
                    test123,
                    true,
                    false,
                    new HashSet<>(),
                    new HashSet<>(),
                    objects
            ));
        }
    }
}
