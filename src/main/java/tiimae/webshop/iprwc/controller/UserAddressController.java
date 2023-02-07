package tiimae.webshop.iprwc.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

@RestController
@AllArgsConstructor
public class UserAddressController {
    private UserAddressDAO userAddressDAO;

    @GetMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService get(@PathVariable UUID userAddressId) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.get(userAddressId).get());
    }

    @GetMapping(value = ApiConstant.getAllUserAddressesByUser)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getByUserId(@PathVariable UUID userId) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.getByUserId(userId));
    }

    @PostMapping(value = ApiConstant.getAllUserAddresses)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService create(@RequestBody UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.create(userAddressDTO));
    }

    @PutMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService update(@PathVariable UUID userAddressId, @RequestBody UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.update(userAddressId, userAddressDTO));
    }

    @DeleteMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService delete(@PathVariable UUID userAddressId) throws EntryNotFoundException {
        this.userAddressDAO.remove(userAddressId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Address has been removed!");
    }
}
