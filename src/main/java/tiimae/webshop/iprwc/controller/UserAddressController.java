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
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.UserAddressValidator;

@RestController
@AllArgsConstructor
public class UserAddressController {
    private UserAddressDAO userAddressDAO;
    private UserAddressValidator userAddressValidator;

    @GetMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService get(@PathVariable String userAddressId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.userAddressValidator.checkIfStringIsUUID(userAddressId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.get(id));
    }

    @GetMapping(value = ApiConstant.getAllUserAddressesByUser)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getByUserId(@PathVariable String userId) throws NotAValidUUIDException {
        final UUID id = this.userAddressValidator.checkIfStringIsUUID(userId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.getByUserId(id));
    }

    @PostMapping(value = ApiConstant.getAllUserAddresses)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService create(@RequestBody UserAddressDTO userAddressDTO) throws EntryNotFoundException, InvalidDtoException {
        this.userAddressValidator.validateDTO(userAddressDTO);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.create(userAddressDTO));
    }

    @PutMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService update(@PathVariable String userAddressId, @RequestBody UserAddressDTO userAddressDTO) throws EntryNotFoundException, NotAValidUUIDException, InvalidDtoException {
        final UUID id = this.userAddressValidator.checkIfStringIsUUID(userAddressId);
        this.userAddressValidator.validateDTO(userAddressDTO);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.update(id, userAddressDTO));
    }

    @DeleteMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    @Secured(RoleEnum.User.CODENAME)
    public ApiResponseService delete(@PathVariable String userAddressId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.userAddressValidator.checkIfStringIsUUID(userAddressId);
        this.userAddressDAO.remove(id);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Address has been removed!");
    }
}
