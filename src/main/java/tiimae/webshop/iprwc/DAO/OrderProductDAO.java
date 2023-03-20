package tiimae.webshop.iprwc.DAO;

import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.OrderProductRepository;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.OrderProductMapper;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderProductDAO {

    private OrderProductRepository orderProductRepository;
    private OrderProductMapper orderProductMapper;

    public Optional<OrderProduct> get(UUID id) {
        return this.orderProductRepository.findById(id);
    }

    public OrderProduct create(JSONObject product, Order order) throws EntryNotFoundException {
        return this.orderProductRepository.save(this.orderProductMapper.toOrderProduct(product, order));
    }
}
