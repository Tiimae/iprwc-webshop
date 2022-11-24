package tiimae.webshop.iprwc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.repo.RoleRepository;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.LoginDTO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.security.util.JWTUtil;
import tiimae.webshop.iprwc.service.ApiResponseService;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(
        headers = "Accept=application/json",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {

    private UserRepository userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private RoleRepository roleRepository;

    public AuthController(UserMapper userMapper, UserRepository userRepo, RoleRepository roleRepository){
        this.userMapper = userMapper;
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
    }

    @PostMapping(ApiConstant.register)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService<HashMap<String, String>> register(@RequestBody UserDTO userDTO) {
        final Optional<User> foundUser = userRepo.findByEmail(userDTO.getEmail());

        if (foundUser.isPresent()) {
            return new ApiResponseService(HttpStatus.FOUND, "This email address is already been found");
        }

        userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));

        final User user = this.userMapper.toUser(userDTO);
        user.addRole(this.roleRepository.findByName("User").get());

        final User savedUser = this.userRepo.save(user);

        final ArrayList<String> roles = new ArrayList<>();
        for (Role role : savedUser.getRoles()) {
            roles.add(role.getName());
        }

        String jwtToken = this.jwtUtil.generateToken(savedUser.getEmail(), roles);

        final HashMap<String, String> newUserData = new HashMap<>();
        newUserData.put("jwtToken", jwtToken);
        newUserData.put("userId", String.valueOf(savedUser.getId()));

        return new ApiResponseService<>(HttpStatus.CREATED, newUserData);
    }

    @PostMapping(ApiConstant.login)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService<HashMap<String, String>> login(@RequestBody LoginDTO loginDTO) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        this.authManager.authenticate(authInputToken);

        final Optional<User> loggedUser = this.userRepo.findByEmail(loginDTO.getEmail());

        if (loggedUser.isEmpty()) {
            return new ApiResponseService<>(HttpStatus.UNAUTHORIZED, "The credentials doesnt match");
        }

        final ArrayList<String> roles = new ArrayList<>();
        for (Role role : loggedUser.get().getRoles()) {
            roles.add(role.getName());
        }

        String token = this.jwtUtil.generateToken(loginDTO.getEmail(), roles);

        HashMap<String, String> userData = new HashMap<>();
        userData.put("jwtToken", token);
        userData.put("userId", String.valueOf(loggedUser.get().getId()));

        return new ApiResponseService<>(HttpStatus.ACCEPTED, userData);
    }
}
