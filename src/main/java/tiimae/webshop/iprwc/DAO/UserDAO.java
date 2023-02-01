package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;

@Component
public class UserDAO {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserAddressDAO userAddressDAO;
    private OrderDAO orderDAO;

    public UserDAO(UserRepository userRepository, UserMapper userMapper, @Lazy UserAddressDAO userAddressDAO, @Lazy OrderDAO orderDAO) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userAddressDAO = userAddressDAO;
        this.orderDAO = orderDAO;
    }

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
        final Optional<User> user = this.getUser(id);

        if (user.isEmpty()) {
            return;
        }

        user.get().setDeleted(true);
        this.updateByObject(id, user.get());
    }

    public int verifyUser(UUID userId) {
        return userRepository.verifyUser(userId);
    }

    public int resetUser(UUID userId) {
        return userRepository.resetUser(userId);
    }
}
