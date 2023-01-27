package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kong.unirest.json.JSONArray;
import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.OrderDAO;
import tiimae.webshop.iprwc.DAO.OrderProductDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.service.OrderService;
import tiimae.webshop.iprwc.validators.OrderValidator;

@RestController
@AllArgsConstructor
public class OrderController {

    private OrderDAO orderDAO;
    private OrderProductDAO orderProductDAO;
    private OrderService orderService;
    private OrderValidator orderValidator;

    @PostMapping(value = ApiConstant.getAllOrders)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService create(
            @RequestParam(value = "invoice") String invoiceId,
            @RequestParam(value = "delivery") String deliveryId,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "products") JSONArray productIds
    ) throws EntryNotFoundException {
        final OrderDTO orderDTO = this.orderService.toDTO(invoiceId, deliveryId, userId, new String[0]);

        String validateDTO = this.orderValidator.validateDTO(orderDTO);

        if (validateDTO != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateDTO);
        }

        String validateOrderProducts = this.orderValidator.validateOrderProducts(productIds);

        if (validateOrderProducts != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateOrderProducts);
        }

        final Order order = this.orderDAO.create(orderDTO);

        for (int i = 0; i < productIds.length(); i++) {
            this.orderProductDAO.create(productIds.getJSONObject(i), order);
        }

        return new ApiResponseService(HttpStatus.CREATED, order);
    }

}
