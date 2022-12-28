package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.constants.ApiConstant;
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
    public ApiResponseService getOne(UUID userAddressId) {
        return new ApiResponseService(HttpStatus.ACCEPTED, "");
    }

    @GetMapping(value = ApiConstant.getAllUserAddresses)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userAddressDAO.getAll());
    }
}
