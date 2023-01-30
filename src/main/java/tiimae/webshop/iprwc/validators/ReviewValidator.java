package tiimae.webshop.iprwc.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.ReviewDTO;

@Component
public class ReviewValidator extends Validator {

   public ReviewValidator(ProductDAO productDAO) {
      super(productDAO);
   }

   public String validateDTO(ReviewDTO reviewDTO) {

      if (reviewDTO.getStars() < 0 && reviewDTO.getStars() > 5) {
         return "Stars can't be lower than 0 or higher than 5";
      }

      String checkIfStringIsUUID = this.checkIfStringIsUUID(reviewDTO.getProductId());

      if (checkIfStringIsUUID != null) {
         return checkIfStringIsUUID;
      }

      String checkIfProductExists = this.CheckIfProductExists(UUID.fromString(reviewDTO.getProductId()));

      if (checkIfProductExists != null) {
         return checkIfProductExists;
      }

      return null;
   }
   
}
