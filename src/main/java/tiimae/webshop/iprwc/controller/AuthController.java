package tiimae.webshop.iprwc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
import tiimae.webshop.iprwc.service.EncryptionService;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

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

    @Value("${jwt_secret}")
    private String jwtSecret;

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
            return new ApiResponseService(HttpStatus.BAD_REQUEST, "Something went Wrong!");
        }

        userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));

        final User user = this.userMapper.toUser(userDTO);
        user.addRole(this.roleRepository.findByName("User").get());

        final User savedUser = this.userRepo.save(user);

        final ArrayList<String> roles = new ArrayList<>();
        for (Role role : savedUser.getRoles()) {
            roles.add(role.getName());
        }

        String jwtToken = this.jwtUtil.generateToken(savedUser.getId(), savedUser.getEmail(), roles);

        final HashMap<String, String> newUserData = new HashMap<>();
        newUserData.put("jwtToken", jwtToken);
        newUserData.put("userId", String.valueOf(savedUser.getId()));
        newUserData.put("destination", "to-cookie");

        return new ApiResponseService<>(HttpStatus.CREATED, newUserData);
    }

    @PostMapping(ApiConstant.login)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService login(@RequestBody LoginDTO loginDTO) throws AuthenticationException, IOException {
        HashMap<String, String> userData = new HashMap<>();
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        try {
            this.authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            userData.put("message", "You have entered an invalid email or password");
            return new ApiResponseService<HashMap<String, String>>(HttpStatus.UNAUTHORIZED, userData);
        }

        final Optional<User> loggedUser = this.userRepo.findByEmail(loginDTO.getEmail());

        if (loggedUser.isEmpty()) {
            userData.put("message", "An error has occured, please try again in a moment");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, userData);
        }

        final ArrayList<String> roles = new ArrayList<>();
        for (Role role : loggedUser.get().getRoles()) {
            roles.add(role.getName());
        }

        String token = this.jwtUtil.generateToken(loggedUser.get().getId(), loginDTO.getEmail(), roles);

        userData.put("jwtToken", token);
        userData.put("userId", String.valueOf(loggedUser.get().getId()));
        userData.put("destination", "to-cookie");

        return new ApiResponseService<>(HttpStatus.ACCEPTED, userData);
    }

    @GetMapping(value = ApiConstant.toCookie, consumes = MediaType.ALL_VALUE)
    public ModelAndView redirectWithUsingForwardPrefix(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("attribute", "forwardWithForwardPrefix");
        response.addCookie(this.createCookie());
        return new ModelAndView("redirect:" + request.getHeader(HttpHeaders.REFERER) + "", model);
    }

    @GetMapping(value = ApiConstant.secret, consumes = MediaType.ALL_VALUE)
    @ResponseBody()
    public ApiResponseService secret(HttpServletRequest request){
        String secret = null;

        if(request.getCookies() != null){
            secret =  Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("secret"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if(secret == null || secret.isBlank() || secret.isEmpty()){
            return new ApiResponseService(HttpStatus.FORBIDDEN, "You are not authenticated");
        }

        secret = new EncryptionService().decrypt(secret, jwtSecret);

        return new ApiResponseService(HttpStatus.ACCEPTED, secret);
    }

    public String createSecret(){
        String randomSecret = Date.from(Instant.now()).toString() + String.valueOf((new Random()).nextInt());
        return EncryptionService.getMd5(randomSecret);
    }

    private Cookie createCookie(){
        String secret = this.createSecret();
        secret = new EncryptionService().encrypt(secret, jwtSecret);
        Cookie cookie = new Cookie("secret", secret);

        cookie.setHttpOnly(true);
        cookie.setPath(ApiConstant.secret);
        //expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.set
        cookie.setDomain("localhost");
        return cookie;
    }
}
