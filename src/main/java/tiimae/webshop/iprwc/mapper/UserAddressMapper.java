package tiimae.webshop.iprwc.mapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;

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

    public UserAddress mergeUserAddress(UserAddress base, UserAddressDTO update) {
        base.setStreet(update.getStreet());
        base.setHouseNumber(update.getHouseNumber());
        base.setAddition(update.getAddition());
        base.setZipcode(update.getZipcode());
        base.setCity(update.getCity());
        base.setCountry(update.getCountry());

        return base;
    }

    private User getUser(UUID userId) throws EntryNotFoundException {
        User user = null;

        if (userId != null) {
            user = this.userDAO.getUser(userId);
        }

        return user;
    }

}
