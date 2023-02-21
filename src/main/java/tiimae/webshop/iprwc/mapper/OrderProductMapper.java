package tiimae.webshop.iprwc.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kong.unirest.json.JSONObject;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;
import tiimae.webshop.iprwc.models.Product;

@Component
public class OrderProductMapper {

    private ProductDAO productDAO;

    public OrderProductMapper(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public OrderProduct toOrderProduct(JSONObject product, Order order) throws EntryNotFoundException {
        return new OrderProduct(product.getLong("amount"), "delivered", order, this.getProduct(UUID.fromString(product.getString("id"))));
    }

    public Product getProduct(UUID id) throws EntryNotFoundException {
        Product product = null;
        if (id != null) {
            product = this.productDAO.get(id);
        }
        return product;

    }

}
