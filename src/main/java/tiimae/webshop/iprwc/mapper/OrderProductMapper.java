package tiimae.webshop.iprwc.mapper;

import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;
import tiimae.webshop.iprwc.models.Product;

import java.util.UUID;

@Component
public class OrderProductMapper {

    private ProductDAO productDAO;

    public OrderProductMapper(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public OrderProduct toOrderProduct(JSONObject product, Order order) {
        return new OrderProduct(product.getLong("amount"), "delivered", order, this.getProduct(UUID.fromString(product.getString("id"))));
    }

    public Product getProduct(UUID id) {
        Product product = null;
        if (id != null) {
            product = this.productDAO.get(id);
        }
        return product;

    }

}
