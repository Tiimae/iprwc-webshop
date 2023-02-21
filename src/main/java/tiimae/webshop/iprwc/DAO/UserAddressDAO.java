package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.UserAddressRepository;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.UserAddressMapper;
import tiimae.webshop.iprwc.models.Supplier;
import tiimae.webshop.iprwc.models.UserAddress;

@Component
public class UserAddressDAO {

    private UserAddressRepository userAddressRepository;
    private UserAddressMapper userAddressMapper;

    public UserAddressDAO(UserAddressRepository userAddressRepository,  UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    public UserAddress get(UUID id) throws EntryNotFoundException {
        final Optional<UserAddress> byId = this.userAddressRepository.findById(id);
        this.checkIfExists(byId);
        return byId.get();
    }

    public List<UserAddress> getByUserId(UUID userId) {
        return this.userAddressRepository.findAllByUserId(userId);
    }

    public List<UserAddress> getAll() {
        return this.userAddressRepository.findAll();
    }

    public UserAddress create(UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return this.userAddressRepository.save(this.userAddressMapper.toUserAddress(userAddressDTO));
    }

    public UserAddress update(UUID id, UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        final Optional<UserAddress> byId = this.userAddressRepository.findById(id);
        this.checkIfExists(byId);
        return byId.map(userAddress -> this.userAddressRepository.saveAndFlush(this.userAddressMapper.mergeUserAddress(userAddress, userAddressDTO))).orElse(null);
    }

    public void remove(UUID id) throws EntryNotFoundException {
        final Optional<UserAddress> byId = this.userAddressRepository.findById(id);
        this.checkIfExists(byId);

        final UserAddress userAddress = byId.get();

        userAddress.setUser(null);

        userAddress.getOrders().clear();

        this.userAddressRepository.delete(userAddress.getId());
    }

    public void checkIfExists(Optional<UserAddress> userAddress) throws EntryNotFoundException {
        if (userAddress.isEmpty()) {
            throw new EntryNotFoundException("This address has not been found!");
        }
    }
}
