package tiimae.webshop.iprwc.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.TokenDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.token.TokenNotFoundException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.auth.PasswordGeneratorService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserDAO userDAO;
    private UserMapper userMapper;
    private PasswordGeneratorService passwordGeneratorService;
    private UserValidator userValidator;
    private TokenDAO tokenDAO;

    @GetMapping(ApiConstant.getOneUser)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService get(@PathVariable String userId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.userValidator.checkIfStringIsUUID(userId);
        final User user = this.userDAO.getUser(id);

        if (user.getDeleted()) {
            throw new EntryNotFoundException("User has not been found!");
        }

        final User user1 = user;
        user1.getRoles().clear();

        return new ApiResponseService<>(HttpStatus.ACCEPTED, user1);
    }

    @SneakyThrows
    @GetMapping(ApiConstant.getOneUserWithRole)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getWithRoles(@PathVariable String userId) throws EntryNotFoundException, NotAValidUUIDException {

        final UUID id = this.userValidator.checkIfStringIsUUID(userId);
        final User user = this.userDAO.getUser(id);

        if (user.getDeleted()) {
            throw new EntryNotFoundException("User has not been found!");
        }

        final User user1 = user;

        for (Role role: user1.getRoles()) {
            role.getUsers().clear();
        }

        user.getOrders().clear();
        user.getAddresses().clear();

        return new ApiResponseService<>(HttpStatus.ACCEPTED, user1);
    }

    @GetMapping(ApiConstant.getUsersWithRoles)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    public ApiResponseService getUsersWithRoles() {
        final List<User> allUsers = this.userDAO.getAllUsers();
        final ArrayList<User> returnUsers = new ArrayList<>();

        for (User user : allUsers) {
            if (!user.getDeleted()) {
                returnUsers.add(user);
            }
        }

        return new ApiResponseService<>(HttpStatus.ACCEPTED, returnUsers);
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
    public ApiResponseService create(@RequestBody UserDTO userDTO) throws InvalidDtoException, EntryAlreadyExistsException {

        this.userValidator.validateDTO(userDTO);

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
    public ApiResponseService update(@PathVariable String userId, @RequestBody UserDTO userDTO) throws EntryNotFoundException, NotAValidUUIDException, InvalidDtoException, EntryAlreadyExistsException {

        userDTO.setRoleIds(new String[0]);

        final UUID id = this.userValidator.checkIfStringIsUUID(userId);
        this.userValidator.validateDTO(userDTO);
        final User update = this.userDAO.update(id, userDTO);
        update.getRoles().clear();

        return new ApiResponseService(HttpStatus.ACCEPTED, update);
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
    public ApiResponseService updateAdmin(@PathVariable String userId, @RequestBody UserDTO userDTO) throws EntryNotFoundException, NotAValidUUIDException, InvalidDtoException, EntryAlreadyExistsException {

        final UUID id = this.userValidator.checkIfStringIsUUID(userId);
        this.userValidator.validateDTO(userDTO);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.userDAO.update(id, userDTO));
    }

    @DeleteMapping(
            value = ApiConstant.getOneUser
    )
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService delete(@PathVariable String userId) throws TokenNotFoundException, EntryNotFoundException, NotAValidUUIDException {

        UUID id = this.userValidator.checkIfStringIsUUID(userId);

        this.tokenDAO.deleteTokensByUserId(UUID.fromString(userId));
        this.userDAO.delete(UUID.fromString(userId));

        return new ApiResponseService(HttpStatus.ACCEPTED, "User has been deleted");
    }

    @GetMapping(value = ApiConstant.getOneUserHasRole)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getIfHasRoles(@PathVariable String userId) throws NotAValidUUIDException, EntryNotFoundException {

        UUID id = this.userValidator.checkIfStringIsUUID(userId);
        final User user = this.userDAO.getUser(id);

        for (Role role : user.getRoles()) {
            if (role.getName().equals("Admin") || role.getName().equals("Owner")) {
                return new ApiResponseService(HttpStatus.ACCEPTED, true);
            }
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, false);
    }

    @GetMapping(value = ApiConstant.getOneUserVerified)
    @ResponseBody
    @Secured({RoleEnum.Admin.CODENAME, RoleEnum.User.CODENAME})
    @PreAuthorize("@endpointValidator.ensureUserAccessWithOpenEndpoint(#userId, authentication.name)")
    public ApiResponseService getIfVerified(@PathVariable String userId) throws NotAValidUUIDException, EntryNotFoundException {

        UUID id = this.userValidator.checkIfStringIsUUID(userId);
        final User user = this.userDAO.getUser(id);

        return new ApiResponseService(HttpStatus.ACCEPTED, user.getVerified());
    }

}
