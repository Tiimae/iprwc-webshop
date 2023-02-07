package tiimae.webshop.iprwc.validators;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.models.Brand;

@Component
public class BrandValidator extends Validator {

    private BrandDAO brandDAO;

    public BrandValidator(ProductDAO productDAO, BrandDAO brandDAO) {
        super(productDAO);
        this.brandDAO = brandDAO;
    }

    public String validateDTO(BrandDTO brandDTO, UUID id) {

        Optional<Brand> brandByName = this.brandDAO.getBrandByName(brandDTO.getBrandName());

        if (brandByName.isPresent()) {
            if (id != null) {
                if (brandByName.get().getId() != id) {
                    return "Brand with name: " + brandDTO.getBrandName() + " already exists!";
                }
            } else {
                return "Brand with name: " + brandDTO.getBrandName() + " already exists!";
            }
        }

        if (!this.URL_REGEX_PATTERN.matcher(brandDTO.getWebPage()).matches()) {
            return "The string " + brandDTO.getWebPage() + " is not a valid url!";
        }

        for (String productId : brandDTO.getProductIds()) {
            String checkIfStringIsUUID = this.checkIfStringIsUUID(productId);

            if (checkIfStringIsUUID != null) { 
                return checkIfStringIsUUID;
            }

            String checkIfProductExists = this.CheckIfProductExists(UUID.fromString(productId));

            if (checkIfProductExists != null) {
                return checkIfProductExists;
            }
        }

        return null;
    }

    public String validateId(String id) {

        String checkIfStringIsUUID = this.checkIfStringIsUUID(id);

        if (checkIfStringIsUUID != null) { 
            return checkIfStringIsUUID;
        }

        final Brand byName = this.brandDAO.getBrand(UUID.fromString(id));

        if (byName == null) {
            return "The brand with id: " + id + " doesn't exist";
        }

        return null;
    }
}
