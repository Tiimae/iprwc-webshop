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

    public User update(UUID id, UserDTO userDTO) {
        final Optional<User> byId = this.userRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

        userDTO.setPassword(byId.get().getPassword());
        final User user = this.userMapper.toUser(userDTO);
        user.setId(id);

        return this.userRepository.saveAndFlush(user);
    }
}
