package tiimae.webshop.iprwc.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.auth.PasswordGeneratorService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.UserValidator;

@RestController
@AllArgsConstructor
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserDAO userDAO;
    private UserMapper userMapper;
    private PasswordGeneratorService passwordGeneratorService;
    private UserValidator userValidator;

    @GetMapping(ApiConstant.getOneUser)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService get(@PathVariable UUID userId) {
        final Optional<User> user = this.userDAO.getUser(userId);

        if (user.isEmpty()) {
            return new ApiResponseService<>(HttpStatus.NOT_FOUND, "User has not been found!");
        }

        final User user1 = user.get();
        user1.getRoles().clear();

        return new ApiResponseService<>(HttpStatus.FOUND, user1);
    }

    @GetMapping(ApiConstant.getOneUserWithRole)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getWithRoles(@PathVariable UUID userId) {
        final Optional<User> user = this.userDAO.getUser(userId);

        if (user.isEmpty()) {
            return new ApiResponseService<>(HttpStatus.NOT_FOUND, "User has not been found!");
        }

        final User user1 = user.get();

        for (Role role: user1.getRoles()) {
            role.getUsers().clear();
        }

        return new ApiResponseService<>(HttpStatus.FOUND, user1);
    }

    @GetMapping(ApiConstant.getUsersWithRoles)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    public ApiResponseService getUsersWithRoles() {
        final List<User> allUsers = this.userDAO.getAllUsers();

        return new ApiResponseService<>(HttpStatus.OK, allUsers);
    }

    @PostMapping(
            value = ApiConstant.getAllUsers,
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService create(@RequestBody UserDTO userDTO) {

        String validateDTO = this.userValidator.validateDTO(null, userDTO);

        if (validateDTO != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateDTO);
        }

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
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService update(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        userDTO.setRoleIds(new String[0]);

        String validateId = this.userValidator.validateId(userId);

        if (validateId != null) {
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, validateId);
        }

        String validateDTO = this.userValidator.validateDTO(UUID.fromString(userId), userDTO);

        if (validateDTO != null) {
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, validateDTO);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, this.userDAO.update(UUID.fromString(userId), userDTO));
    }

    @PutMapping(
            value = ApiConstant.getOneUserAdmin,
            headers = "Accept=application/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService updateAdmin(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        String validateId = this.userValidator.validateId(userId);

        if (validateId != null) {
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, validateId);
        }

        String validateDTO = this.userValidator.validateDTO(UUID.fromString(userId), userDTO);
        
        if (validateDTO != null) {
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, validateDTO);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, this.userDAO.update(UUID.fromString(userId), userDTO));
    }

    @DeleteMapping(
            value = ApiConstant.getOneUser
    )
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService delete(@PathVariable String userId) {

        String validateId = this.userValidator.validateId(userId);

        if (validateId != null) {
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, validateId);
        }

        this.userDAO.delete(UUID.fromString(userId));

        return new ApiResponseService(HttpStatus.ACCEPTED, "User has been deleted");
    }
}
