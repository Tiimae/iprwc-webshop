package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.UserAddressRepository;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.UserAddressMapper;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserAddressDAO {

    private UserAddressRepository userAddressRepository;
    private UserAddressMapper userAddressMapper;

    public UserAddressDAO(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    public List<UserAddress> getAll() {
        return this.userAddressRepository.findAll();
    }

    public UserAddress create(UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return this.userAddressRepository.save(this.userAddressMapper.toUserAddress(userAddressDTO));
    }

    public UserAddress update(UUID id, UserAddressDTO userAddressDTO) {
        final Optional<UserAddress> byId = this.userAddressRepository.findById(id);

        return byId.map(userAddress -> this.userAddressRepository.saveAndFlush(this.userAddressMapper.mergeUserAddress(userAddress, userAddressDTO))).orElse(null);
    }

    public void remove(UUID id) {
        final Optional<UserAddress> byId = this.userAddressRepository.findById(id);

        if (byId.isEmpty()) {
            return;
        }

        final UserAddress userAddress = byId.get();

        userAddress.setUser(null);

        userAddress.getOrders().clear();

        this.userAddressRepository.delete(userAddress.getId());
    }
}
