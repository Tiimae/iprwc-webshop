package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.SupplierDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class SupplierValidator extends Validator {

    public void validateDTO(SupplierDTO supplierDTO) throws InvalidDtoException {
        if (supplierDTO.getName() == null) {
            throw new InvalidDtoException("Name can not be null");
        }

        if (supplierDTO.getCity() == null) {
            throw new InvalidDtoException("City can not be null");
        }

        if (supplierDTO.getAddress() == null) {
            throw new InvalidDtoException("Address can not be null");
        }

        if (supplierDTO.getCountry() == null) {
            throw new InvalidDtoException("Country can not be null");
        }

        if (supplierDTO.getZipcode() == null) {
            throw new InvalidDtoException("Zipcode can not be null");
        }
    }
   
}
