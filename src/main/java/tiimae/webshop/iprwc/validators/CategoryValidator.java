package tiimae.webshop.iprwc.validators;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.models.Category;

@Component
public class CategoryValidator extends Validator {

    private CategoryDAO categoryDAO;

    public CategoryValidator(ProductDAO productDAO, CategoryDAO categoryDAO) {
        super(productDAO);
        this.categoryDAO = categoryDAO;
    }

    public String validateDTO(CategoryDTO categoryDTO) {
        final Optional<Category> byName = this.categoryDAO.getByName(categoryDTO.getCategoryName());

        if (!byName.isEmpty()) {
            return "Category name already exists";
        }

        for (String productId : categoryDTO.getProductIds()) {
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

        final Category byName = this.categoryDAO.get(UUID.fromString(id));

        if (byName == null) {
            return "The category with id: " + id + " doesn't exist";
        }

        return null;
    }

}
