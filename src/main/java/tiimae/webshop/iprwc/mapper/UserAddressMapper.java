package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserAddressMapper {

    private UserDAO userDAO;

    public UserAddressMapper(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserAddress toUserAddress(UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return new UserAddress(
                userAddressDTO.getStreet(),
                userAddressDTO.getHouseNumber(),
                userAddressDTO.getAddition(),
                userAddressDTO.getZipcode(),
                userAddressDTO.getCity(),
                userAddressDTO.getCountry(),
                userAddressDTO.getType(),
                this.getUser(userAddressDTO.getUserId()),
                new HashSet<>()
        );
    }

    private User getUser(UUID userId) throws EntryNotFoundException {
        User user = null;

        if (userId != null) {
            final Optional<User> user1 = this.userDAO.getUser(userId);

            if (user1.isEmpty()) {
                throw new EntryNotFoundException("User has not been found!");
            }

            user = user1.get();
        }

        return user;
    }

}
