package tiimae.webshop.iprwc.validators;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;


@Component
public class OrderValidator extends Validator {

   public void validateDTO(OrderDTO orderDTO) throws InvalidDtoException {
      if (orderDTO.getAddressIds().length != 2) {
         throw new InvalidDtoException("There need to be 2 addresses specified");
      }

      if (orderDTO.getUserId() == null) {
         throw new InvalidDtoException("A user needs to be specified");
      }

      if (orderDTO.getProductIds().length < 1) {
         throw new InvalidDtoException("There need to be more than one product");
      }
   }

   public void validateOrderProducts(JSONObject product) throws InvalidDtoException {

      if (product.getLong("amount") < 1) {
         throw new InvalidDtoException("Product amount can't be smaller than 1");
      }

   }

}
