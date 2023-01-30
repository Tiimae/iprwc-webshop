package tiimae.webshop.iprwc.DAO;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.OrderProductRepository;
import tiimae.webshop.iprwc.mapper.OrderProductMapper;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;

@Component
@AllArgsConstructor
public class OrderProductDAO {

    private OrderProductRepository orderProductRepository;
    private OrderProductMapper orderProductMapper;

    public Optional<OrderProduct> get(UUID id) {
        return this.orderProductRepository.findById(id);
    }

    public OrderProduct create(JSONObject product, Order order) {
        return this.orderProductRepository.save(this.orderProductMapper.toOrderProduct(product, order));
    }
}
