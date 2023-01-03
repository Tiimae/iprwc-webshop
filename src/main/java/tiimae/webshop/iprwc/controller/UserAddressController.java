package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.DTO.UserAddressDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.util.UUID;

@RestController
public class UserAddressController {
    private UserAddressDAO userAddressDAO;

    public UserAddressController(UserAddressDAO userAddressDAO) {
        this.userAddressDAO = userAddressDAO;
    }

    @GetMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    public ApiResponseService get(UUID userAddressId) {
        return new ApiResponseService(HttpStatus.ACCEPTED, "");
    }

    @GetMapping(value = ApiConstant.getAllUserAddresses)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.getAll());
    }

    @PostMapping(value = ApiConstant.getAllUserAddresses)
    @ResponseBody
    public ApiResponseService create(@RequestBody UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.CREATED, this.userAddressDAO.create(userAddressDTO));
    }

    @PutMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    public ApiResponseService update(@PathVariable UUID userAddressId, @RequestBody UserAddressDTO userAddressDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.CREATED, this.userAddressDAO.update(userAddressId, userAddressDTO));
    }

    @DeleteMapping(value = ApiConstant.getOneUserAddress)
    @ResponseBody
    public ApiResponseService delete(@PathVariable UUID userAddressId) throws EntryNotFoundException {
        this.userAddressDAO.remove(userAddressId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Address has been removed!");
    }
}
