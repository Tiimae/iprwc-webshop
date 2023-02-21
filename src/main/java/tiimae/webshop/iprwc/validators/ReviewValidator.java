package tiimae.webshop.iprwc.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;

@Component
public class ReviewValidator extends Validator {

   public void validateDTO(ReviewDTO reviewDTO) throws InvalidDtoException, NotAValidUUIDException {
      if (reviewDTO.getStars() == null) {
         throw new InvalidDtoException("The stars cannot be null");
      }

      if (reviewDTO.getDescription() == null) {
         throw new InvalidDtoException("The description cannot be null");
      }

      if (reviewDTO.getProductId() == null) {
         throw new InvalidDtoException("The product id cannot be null");
      }

      this.checkIfStringIsUUID(reviewDTO.getProductId());
   }
   
}
