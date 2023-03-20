package tiimae.webshop.iprwc.validators;

import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;


@Component
public class OrderValidator extends Validator {

   public void validateDTO(OrderDTO orderDTO) throws InvalidDtoException {
      if (orderDTO.getAddressIds().length != 2) {
         throw new InvalidDtoException("There need to be 2 addresses specified");
      }

      if (orderDTO.getUserId() == null) {
         throw new InvalidDtoException("A user needs to be specified");
      }
   }

   public void validateOrderProducts(JSONObject product) throws InvalidDtoException {

      if (product.getLong("amount") < 1) {
         throw new InvalidDtoException("Product amount can't be smaller than 1");
      }

   }

}
