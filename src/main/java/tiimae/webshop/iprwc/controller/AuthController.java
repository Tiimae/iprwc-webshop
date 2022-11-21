package tiimae.webshop.iprwc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.security.util.JWTUtil;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(
        headers = "Accept=application/json",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    public AuthController(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @PostMapping(ApiConstant.register)
    @ResponseBody
    public ApiResponseService<HashMap<String, String>> register(@RequestBody UserDTO userDTO) {
        final Optional<User> foundUser = userRepo.findByEmail(userDTO.getEmail());

        if (foundUser.isPresent()) {
            return new ApiResponseService(HttpStatus.FOUND, "This email address is already been found");
        }

        userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        final User savedUser = this.userRepo.save(this.userMapper.toUser(userDTO));

        String jwtToken = this.jwtUtil.generateToken(savedUser.getEmail());

        final HashMap<String, String> newUserData = new HashMap<>();
        newUserData.put("jwtToken", jwtToken);
        newUserData.put("userId", String.valueOf(savedUser.getId()));

        return new ApiResponseService(HttpStatus.CREATED, newUserData);
    }
}
