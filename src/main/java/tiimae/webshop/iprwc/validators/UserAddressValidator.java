package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class UserAddressValidator extends Validator {

    public void validateDTO(UserAddressDTO userAddressDTO) throws InvalidDtoException {
        if (userAddressDTO.getCity() == null) {
            throw new InvalidDtoException("City can not be null");
        }

        if (userAddressDTO.getStreet() == null) {
            throw new InvalidDtoException("Street can not be null");
        }

        if (userAddressDTO.getHouseNumber() == null) {
            throw new InvalidDtoException("House number can not be null");
        }

        if (userAddressDTO.getCountry() == null) {
            throw new InvalidDtoException("Country can not be null");
        }

        if (userAddressDTO.getZipcode() == null) {
            throw new InvalidDtoException("Zipcode can not be null");
        }
    }

}
