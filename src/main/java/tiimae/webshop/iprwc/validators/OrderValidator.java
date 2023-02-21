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
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;


@Component
public class OrderValidator extends Validator {

   public void validateDTO(OrderDTO orderDTO) {

   }

   public void validateOrderProducts(JSONArray products) {

   }

}
