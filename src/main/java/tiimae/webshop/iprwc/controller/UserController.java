package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(
//        headers = "Accept=application/json",
//        consumes = MediaType.APPLICATION_JSON_VALUE,
//        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping(ApiConstant.getOneUser)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService get(@PathVariable UUID userId) {
        final Optional<User> user = this.userDAO.getUser(userId);

        if (user.isEmpty()) {
            return new ApiResponseService<>(HttpStatus.NOT_FOUND, "User has not been found!");
        }

        final User user1 = user.get();
        user1.getRoles().clear();
        user1.getOrders().clear();
        user1.getAddresses().clear();
        user1.setPassword("");

        return new ApiResponseService<>(HttpStatus.FOUND, user1);
    }

    @GetMapping(ApiConstant.getUsersWithRoles)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService getUserWithRoles() {
        final List<User> allUsers = this.userDAO.getAllUsers();

        for (User user : allUsers) {
            user.getAddresses().clear();
            user.getOrders().clear();
            user.setPassword("");
        }

        return new ApiResponseService<>(HttpStatus.OK, allUsers);
    }

}
