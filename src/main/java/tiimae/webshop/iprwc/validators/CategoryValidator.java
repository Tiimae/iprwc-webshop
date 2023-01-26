package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.models.Product;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class CategoryValidator extends Validator {

    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;

    public CategoryValidator(CategoryDAO categoryDAO, ProductDAO productDAO) {
        this.categoryDAO = categoryDAO;
        this.productDAO = productDAO;
    }

    public String validateDTO(CategoryDTO categoryDTO) {
        final Optional<Category> byName = this.categoryDAO.getByName(categoryDTO.getCategoryName());

        if (!byName.isEmpty()) {
            return "Category name already exists";
        }

        for (String productId : categoryDTO.getProductIds()) {
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

        final Category byName = this.categoryDAO.get(UUID.fromString(id));

        if (byName == null) {
            return "The category with id: " + id + " doesn't exist";
        }

        return null;
    }

}
