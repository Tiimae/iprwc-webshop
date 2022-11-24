package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDAO {

    private UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(UUID userId) {
        return this.userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
