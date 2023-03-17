package tiimae.webshop.iprwc.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.CartDAO;
import tiimae.webshop.iprwc.DTO.CartDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.CartValidator;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class CartController {

    private CartDAO cartDAO;
    private CartValidator cartValidator;

    @PostMapping(value = ApiConstant.setNewCartItem)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#cartDTO.getUserId, authentication.name)")
    public ApiResponseService postNewCartItem(@RequestBody CartDTO cartDTO, @RequestParam(required = false) boolean detail) throws EntryNotFoundException, NotAValidUUIDException, InvalidDtoException {
        this.cartValidator.checkIfStringIsUUID(cartDTO.getUserId());
        this.cartValidator.checkIfStringIsUUID(cartDTO.getProductId());
        this.cartValidator.validateDTO(cartDTO);
        this.cartDAO.postCartItemAndReturnMap(cartDTO, detail);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Cart item has been added");
    }

    @GetMapping(value = ApiConstant.getCartById)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getCart(@PathVariable String userId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.cartValidator.checkIfStringIsUUID(userId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.cartDAO.getCart(id));
    }

    @DeleteMapping(value = ApiConstant.removeCartById)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService deleteCartItem(@PathVariable String userId, @PathVariable String productId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID uId = this.cartValidator.checkIfStringIsUUID(userId);
        final UUID pId = this.cartValidator.checkIfStringIsUUID(productId);
        this.cartDAO.deleteFromCart(uId, pId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Item has been removed out of your cart!");
    }

    @DeleteMapping(value = ApiConstant.getCartById)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService deleteCartItem(@PathVariable String userId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.cartValidator.checkIfStringIsUUID(userId);
        this.cartDAO.clearCart(id);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Cart has been cleared");
    }
}
