package tiimae.webshop.iprwc.service;

import java.sql.Date;

import org.springframework.stereotype.Service;

import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;

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

    public String generateHtmlForMail(Order order) {
        String html = "<h1>Your products</h1>" +
                "<table style=\"width:100%\">" +
                "    <thead>" +
                "        <tr>" +
                "            <th>Product</th>" +
                "            <th>amount</th>" +
                "            <th>Price</th>" +
                "        </tr>" +
                "    </thead>" +
                "    <tbody>";

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            html += "<tr>" +
                    "            <td style=\"text-align: center;\">" + orderProduct.getProduct().getProductName() +"</td>" +
                    "            <td style=\"text-align: center;\">" + orderProduct.getAmount() +"</td>" +
                    "            <td style=\"text-align: center;\">€" + orderProduct.getProduct().getPrice() + "</td>" +
                    "        </tr>";
        }

        html += "    </tbody>" +
                "</table>";

        return html;
    }

}
