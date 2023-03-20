package tiimae.webshop.iprwc.service;

import org.springframework.stereotype.Service;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;

import java.sql.Date;

@Service
public class OrderService {

    public OrderDTO toDTO(String invoiceId, String deliveryId, String userId, String[] productIds) {

        long millis = System.currentTimeMillis();

        final OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderDate(new Date(millis));
        orderDTO.setOrderId(String.valueOf(millis));
        orderDTO.setUserId(userId);
        orderDTO.setProductIds(productIds);
        orderDTO.setAddressIds(new String[]{invoiceId, deliveryId});

        return orderDTO;

    }

}
