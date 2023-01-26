package tiimae.webshop.iprwc.validators;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.models.Brand;
import tiimae.webshop.iprwc.models.Product;

@Component
public class BrandValidator extends Validator {
    private ProductDAO productDAO;
    private BrandDAO brandDAO;

    public BrandValidator(ProductDAO productDAO, BrandDAO brandDAO) {
        this.productDAO = productDAO;
        this.brandDAO = brandDAO;
    }

    public String validateDTO(BrandDTO brandDTO) {

        Optional<Brand> brandByName = this.brandDAO.getBrandByName(brandDTO.getBrandName());

        if (!brandByName.isEmpty()) {
            return "Brand with name: " + brandDTO.getBrandName() + " already exists!";
        }

        if (!this.URL_REGEX_PATTERN.matcher(brandDTO.getWebPage()).matches()) {
            return "The string " + brandDTO.getWebPage() + " is not a valid url!";
        }

        for (String productId : brandDTO.getProductIds()) {
            if (!this.UUID_REGEX_PATTERN.matcher(productId).matches()) {
                return productId + " is not a valid UUID";
            }

            final Product product = this.productDAO.get(UUID.fromString(productId));

            if (product == null) {
                return "Product " + productId + " has not been found!";
            }
        }

        return null;
    }

    public String validateId(String id) {

        if (!this.UUID_REGEX_PATTERN.matcher(id).matches()) {
            return id + " is not a valid UUID";
        }

        final Brand byName = this.brandDAO.getBrand(UUID.fromString(id));

        if (byName == null) {
            return "The brand with id: " + id + " doesn't exist";
        }

        return null;
    }
}
