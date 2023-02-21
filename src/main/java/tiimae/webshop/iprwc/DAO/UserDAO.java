package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.User;

@Component
public class UserDAO {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private OrderDAO orderDAO;

    public UserDAO(UserRepository userRepository, @Lazy UserMapper userMapper, @Lazy OrderDAO orderDAO) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.orderDAO = orderDAO;
    }

    public User getUser(UUID userId) throws EntryNotFoundException {
        final Optional<User> byId = this.userRepository.findById(userId);
        this.checkIfExists(byId);

        return byId.get();
    }

    public User getByEmail(String email) throws EntryNotFoundException {
        final Optional<User> byId = this.userRepository.findByEmail(email);
        this.checkIfExists(byId);
        return byId.get();
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User create(User user) {
        return this.userRepository.save(user);
    }

    public User update(UUID id, UserDTO userDTO) throws EntryNotFoundException {
        final Optional<User> byId = this.userRepository.findById(id);
        this.checkIfExists(byId);

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

    public void delete(UUID id) throws EntryNotFoundException {
        User user = this.getUser(id);

        user.setDeleted(true);
        this.updateByObject(id, user);
    }

    public int verifyUser(UUID userId) {
        return userRepository.verifyUser(userId);
    }

    public int resetUser(UUID userId) {
        return userRepository.resetUser(userId);
    }

    public void checkIfExists(Optional<User> user) throws EntryNotFoundException {
        if (user.isEmpty()) {
            throw new EntryNotFoundException("This address has not been found!");
        }
    }
}
