package tiimae.webshop.iprwc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.ApiResponseService;
import tiimae.webshop.iprwc.service.PasswordGeneratorService;

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

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserDAO userDAO;
    private UserMapper userMapper;
    private PasswordGeneratorService passwordGeneratorService = new PasswordGeneratorService();

    public UserController(UserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    @GetMapping(ApiConstant.getOneUser)
    @ResponseBody
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

    @GetMapping(ApiConstant.getOneUserWithRole)
    @ResponseBody
    public ApiResponseService getWithRoles(@PathVariable UUID userId) {
        final Optional<User> user = this.userDAO.getUser(userId);

        if (user.isEmpty()) {
            return new ApiResponseService<>(HttpStatus.NOT_FOUND, "User has not been found!");
        }

        final User user1 = user.get();
        user1.getOrders().clear();
        user1.getAddresses().clear();
        user1.setPassword("");

        for (Role role: user1.getRoles()) {
            role.getUsers().clear();
        }

        return new ApiResponseService<>(HttpStatus.FOUND, user1);
    }

    @GetMapping(ApiConstant.getUsersWithRoles)
    @ResponseBody
    public ApiResponseService getUsersWithRoles() {
        final List<User> allUsers = this.userDAO.getAllUsers();

        for (User user : allUsers) {
            user.getOrders().clear();
            user.setPassword("");
        }

        return new ApiResponseService<>(HttpStatus.OK, allUsers);
    }

    @PostMapping(
            value = ApiConstant.getAllUsers,
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @CrossOrigin
    public ApiResponseService create(@RequestBody UserDTO userDTO) {

        final String password = passwordGeneratorService.generateNewPassword();

        userDTO.setPassword(this.passwordEncoder.encode(password));

        final User user = this.userDAO.create(this.userMapper.toUser(userDTO));
        user.setPassword("");

        return new ApiResponseService(HttpStatus.ACCEPTED, user);
    }


    @PutMapping(
            value = ApiConstant.getOneUser,
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @CrossOrigin
    public ApiResponseService update(@PathVariable UUID userId, @RequestBody UserDTO userDTO) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.userDAO.update(userId, userDTO));
    }

    @DeleteMapping(
            value = ApiConstant.getOneUser
    )
    @ResponseBody
    @CrossOrigin
    public ApiResponseService delete(@PathVariable UUID userId) {

        this.userDAO.delete(userId);

        return new ApiResponseService(HttpStatus.ACCEPTED, "User has been deleted");
    }
}
