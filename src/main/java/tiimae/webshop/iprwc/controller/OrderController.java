package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.DAO.OrderDAO;
import tiimae.webshop.iprwc.DAO.OrderProductDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;
import tiimae.webshop.iprwc.service.ApiResponseService;
import tiimae.webshop.iprwc.service.OrderService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrderController {

    private OrderDAO orderDAO;
    private OrderProductDAO orderProductDAO;
    private OrderService orderService;

    public OrderController(OrderDAO orderDAO, OrderProductDAO orderProductDAO, OrderService orderService) {
        this.orderDAO = orderDAO;
        this.orderProductDAO = orderProductDAO;
        this.orderService = orderService;
    }

    @PostMapping(value = ApiConstant.getAllOrders)
    @ResponseBody
    public ApiResponseService create(
            @RequestParam(value = "invoice") UUID invoiceId,
            @RequestParam(value = "delivery") UUID deliveryId,
            @RequestParam(value = "userId") UUID userId,
            @RequestParam(value = "products") JSONArray productIds
    ) throws EntryNotFoundException {
        final OrderDTO orderDTO = this.orderService.toDTO(invoiceId, deliveryId, userId, new UUID[0]);
        final Order order = this.orderDAO.create(orderDTO);

        for (int i = 0; i < productIds.length(); i++) {
            this.orderProductDAO.create(productIds.getJSONObject(i), order);
        }

        return new ApiResponseService(HttpStatus.CREATED, order);
    }

}
