package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.UserAddressRepository;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.UserAddressMapper;
import tiimae.webshop.iprwc.models.UserAddress;

import java.util.List;

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
}
