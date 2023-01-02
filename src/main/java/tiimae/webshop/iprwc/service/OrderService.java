package tiimae.webshop.iprwc.service;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.OrderDTO;

import java.sql.Date;
import java.util.UUID;

@Component
public class OrderService {

    public OrderDTO toDTO(UUID invoiceId, UUID deliveryId, UUID userId, UUID[] productIds) {

        long millis = System.currentTimeMillis();


        final OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderDate(new Date(millis));
        orderDTO.setOrderId(String.valueOf(millis));
        orderDTO.setUserId(userId);
        orderDTO.setProductIds(productIds);
        orderDTO.setAddressIds(new UUID[]{invoiceId, deliveryId});

        return orderDTO;

    }

}
