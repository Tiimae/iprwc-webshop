package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.User;

@Component
@AllArgsConstructor
public class UserDAO {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public Optional<User> getUser(UUID userId) {
        return this.userRepository.findById(userId);
    }

    public Optional<User> getByEmail(String email) {
        return this.userRepository.findByEmail(email);
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

        if (userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(byId.get().getPassword());
        }

        final User user = this.userMapper.mergeUser(byId.get(), userDTO);

        return this.userRepository.saveAndFlush(user);
    }

    public User updateByObject(UUID id, User user) {
        final Optional<User> byId = this.userRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

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
