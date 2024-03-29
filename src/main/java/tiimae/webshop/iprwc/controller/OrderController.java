package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONArray;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.OrderDAO;
import tiimae.webshop.iprwc.DAO.OrderProductDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.service.EmailService;
import tiimae.webshop.iprwc.service.OrderService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.OrderValidator;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class OrderController {

    private OrderDAO orderDAO;
    private OrderProductDAO orderProductDAO;
    private OrderService orderService;
    private OrderValidator orderValidator;
    private EmailService emailService;

    @GetMapping(value = ApiConstant.getOrderByUserId)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService get(@PathVariable String userId) throws NotAValidUUIDException {
        this.orderValidator.checkIfStringIsUUID(userId);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.orderDAO.getByUserId(UUID.fromString(userId)));
    }

    @PostMapping(value = ApiConstant.getAllOrders)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService create(
            @RequestParam(value = "invoice") String invoiceId,
            @RequestParam(value = "delivery") String deliveryId,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "products") JSONArray productIds
    ) throws EntryNotFoundException, InvalidDtoException {

        final OrderDTO orderDTO = this.orderService.toDTO(invoiceId, deliveryId, userId, new String[0]);

        this.orderValidator.validateDTO(orderDTO);

        for (int i = 0; i < productIds.length(); i++) {
            this.orderValidator.validateOrderProducts(productIds.getJSONObject(i));
        }

        final Order order = this.orderDAO.create(orderDTO);

        for (int i = 0; i < productIds.length(); i++) {
            this.orderProductDAO.create(productIds.getJSONObject(i), order);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, order);
    }

}
