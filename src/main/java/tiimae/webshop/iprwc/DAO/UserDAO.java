package tiimae.webshop.iprwc.DAO;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDAO {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserDAO(UserRepository userRepository, @Lazy UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<User> getUser(UUID userId) {
        return this.userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User create(User user) {
        return this.userRepository.save(user);
    }

    public User update(UUID id, UserDTO userDTO) {
        final Optional<User> byId = this.userRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

        userDTO.setPassword(byId.get().getPassword());
        final User user = this.userMapper.mergeUser(byId.get(), userDTO);

        return this.userRepository.saveAndFlush(user);
    }

    public void delete(UUID id) {
        final Optional<User> byId = this.userRepository.findById(id);

        if (byId.isEmpty()) {
            return;
        }

        byId.get().getRoles().clear();
        this.userRepository.delete(byId.get());
    }

    public int verifyUser(UUID userId) {
        return userRepository.verifyUser(userId);
    }

    public int resetUser(UUID userId) {
        return userRepository.resetUser(userId);
    }
}
