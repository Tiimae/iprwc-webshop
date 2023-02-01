package tiimae.webshop.iprwc.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DAO.VerifyTokenDAO;
import tiimae.webshop.iprwc.DAO.repo.RoleRepository;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.DTO.LoginDTO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;
import tiimae.webshop.iprwc.service.SecretService;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.service.EmailService;
import tiimae.webshop.iprwc.service.EncryptionService;
import tiimae.webshop.iprwc.service.auth.LoginService;
import tiimae.webshop.iprwc.util.JWTUtil;
import tiimae.webshop.iprwc.validators.AuthValidator;

@RestController
@RequestMapping(
        headers = "Accept=application/json",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerifyTokenDAO verifyTokenDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AuthValidator authValidator;
    @Autowired
    private LoginService loginService;

    @Autowired
    private SecretService secretService;

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("${domain}")
    private String base;
    @Value("${shared_secret}")
    private String sharedSecret;

    @PostMapping(value = ApiConstant.register)
    @ResponseBody
    public ApiResponseService register(@Valid @RequestBody UserDTO user, @RequestParam(required = false) boolean encrypted) throws EntryNotFoundException {
        user.setVerified(false);
        user.setResetRequired(false);
        String password = encrypted ? EncryptionService.decryptAes(user.getPassword(), sharedSecret) : user.getPassword();

        String validation = this.authValidator.registerValidation(user.getEmail(), password);

        if (validation != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validation);
        }

        String encodedPass = passwordEncoder.encode(password);

        user.setPassword(encodedPass);
        User newUser = userMapper.toUser(user);
        newUser.addRole(this.roleRepository.findByName("User").get());

        newUser = userRepo.save(newUser);

        return new ApiResponseService<>(HttpStatus.ACCEPTED, "User has been registered");
    }

    @PostMapping(value = ApiConstant.login)
    @ResponseBody
    public ApiResponseService login(@RequestBody LoginDTO user, @RequestParam(required = false) boolean encrypted) throws AuthenticationException, IOException {
        final HashMap<String, String> res = new HashMap<>();

        if (encrypted) {
            user.setPassword(EncryptionService.decryptAes(user.getPassword(), sharedSecret));
        }

        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        try {
            authManager.authenticate(authInputToken);
        } catch (Throwable $throwable) {
            res.put("message", "You have entered an invalid email or password");
            return new ApiResponseService<HashMap<String, String>>(HttpStatus.UNAUTHORIZED, res);
        }

        Optional<User> foundUser = this.userRepo.findByEmail(user.getEmail());

        if (!foundUser.isPresent()) {
            res.put("message", "An error has occured, please try again in a moment");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        this.loginService.generateTokens(foundUser.get());

        res.put("jwtToken", foundUser.get().getAccessToken().getValue());
        res.put("refreshToken", foundUser.get().getRefreshToken().getValue());
        res.put("verified", foundUser.get().getVerified().toString());
        res.put("destination", "to-cookie");

        return new ApiResponseService<>(HttpStatus.OK, res);
    }

    @GetMapping(value = ApiConstant.toCookie, consumes = MediaType.ALL_VALUE)
    public ModelAndView redirectWithUsingForwardPrefix(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("attribute", "forwardWithForwardPrefix");
        response.addCookie(this.secretService.createCookie());
        return new ModelAndView("redirect:" + request.getHeader(HttpHeaders.REFERER) + "", model);
    }

    @GetMapping(value = ApiConstant.secret, consumes = MediaType.ALL_VALUE)
    @ResponseBody()
    public ApiResponseService secret(HttpServletRequest request) {
        String secret = this.secretService.getSecret(request);

        if (secret == null || secret.isBlank() || secret.isEmpty()) {
            return new ApiResponseService(HttpStatus.FORBIDDEN, "You are not authenticated");
        }

        secret = new EncryptionService().decrypt(secret, jwtSecret);

        return new ApiResponseService(HttpStatus.ACCEPTED, secret);
    }

    @GetMapping(value = ApiConstant.sendVerifyEmail, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> sendVerifyEmail() {
        Map<String, Object> res = new HashMap<>();

        Optional<User> bearerUser = this.profile(SecurityContextHolder.getContext().getAuthentication()).getPayload();

        if (!bearerUser.isPresent()) {
            res.put("message", "The user you are trying to verify was not found");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        if (bearerUser.get().getVerified()) {
            res.put("message", "This user is already verified, cant send a verify token");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        User user = bearerUser.get();
        UUID token = UUID.randomUUID();
        VerifyToken verifyToken = new VerifyToken(token, "email", LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        this.verifyTokenDAO.saveVerifyToken(verifyToken);

        try {
            this.emailService.sendMessage(
                    user.getEmail(),
                    "CGI account verify email",
                    "<p>Hi " + user.getFirstName() + ", here is your code to verify your email:" + token + "</p>"
            );
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

        res.put("message", "Successfully sent a verify token to " + user.getEmail());
        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @PostMapping(value = ApiConstant.verifyEmail, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> verifyEmail(@RequestParam UUID token) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> bearerUser = this.profile(SecurityContextHolder.getContext().getAuthentication()).getPayload();
        Optional<VerifyToken> verifyToken = this.verifyTokenDAO.getToken(token);


        if (!bearerUser.isPresent()) {
            res.put("message", "Something went wrong, please try again in a moment");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        if (!verifyToken.isPresent() || !verifyToken.get().getType().equals("email")) {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        UUID bearerUserId = bearerUser.get().getId();
        UUID tokenUserId = verifyToken.get().getUser().getId();

        if (bearerUserId.equals(tokenUserId)) {
            try {
                this.verifyTokenDAO.confirmToken(token);
                res.put("message", "Successfully confirmed your email. Redirecting you...");
                return new ApiResponseService<>(HttpStatus.OK, res);
            } catch (Exception e) {
                res.put("message", e.getMessage());
                return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
            }
        } else {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }
    }

    @PostMapping(value = ApiConstant.forgotPassword, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Map<String, Object>> forgotPassword(@RequestParam String email) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> foundUser = this.userRepo.findByEmail(email);

        if (!foundUser.isPresent()) {
            res.put("message", "The user you are trying to reset the password for was not found");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        User user = foundUser.get();

        // Create and save Token in DB
        UUID token = UUID.randomUUID();
        VerifyToken verifyToken = new VerifyToken(token, "password", LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        this.verifyTokenDAO.saveVerifyToken(verifyToken);

        String url = "http://localhost:4200/auth/set-new-password?token=" + token;

        // Send verification mail
        try {
            this.emailService.sendMessage(
                    user.getEmail(),
                    "Change password",
                    "<p>Hi " + user.getFirstName() + ", you notified us that you want to change your password. Use this link to change your password: <a href=" + url + ">Set new password</a></p>"
            );
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

        res.put("message", "Successfully sent a verify token to " + user.getEmail());
        return new ApiResponseService<>(HttpStatus.ACCEPTED, res);
    }

    @PostMapping(value = ApiConstant.setNewPassword, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Map<String, Object>> setNewPassword(@RequestBody UserDTO newUser, @RequestParam UUID token, @RequestParam(required = false) boolean encrypted) throws EntryNotFoundException {
        Map<String, Object> res = new HashMap<>();

        Optional<VerifyToken> verifyToken = this.verifyTokenDAO.getToken(token);
        Optional<User> foundUser = this.userRepo.findByEmail(newUser.getEmail());

        if (!verifyToken.isPresent() || !verifyToken.get().getType().equals("password")) {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        Optional<User> tokenUser = this.userRepo.findById(verifyToken.get().getUser().getId());

        if (!foundUser.isPresent() || !tokenUser.isPresent()) {
            res.put("message", "The user you are trying to reset the password for was not found");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        UUID foundUserId = foundUser.get().getId();
        UUID tokenUserId = verifyToken.get().getUser().getId();

        if (!foundUserId.equals(tokenUserId)) {
            res.put("message", "This token is invalid");
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

        User user = tokenUser.get();
        // Nieuw wachtwoord setten van user
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setVerified(user.getVerified());
        userDTO.setResetRequired(user.getReset_required());
        String encodedPass = passwordEncoder.encode(
                encrypted
                        ? EncryptionService.decryptAes(newUser.getPassword(), sharedSecret)
                        : newUser.getPassword()
        );
        userDTO.setPassword(encodedPass);

        this.userDAO.update(user.getId(), userDTO);

        try {
            this.verifyTokenDAO.confirmToken(token);
            res.put("message", "Successfully reset your password. Redirecting you...");
            return new ApiResponseService<>(HttpStatus.OK, res);
        } catch (Exception e) {
            res.put("message", e.getMessage());
            return new ApiResponseService<>(HttpStatus.BAD_REQUEST, res);
        }

    }

    @GetMapping(value = ApiConstant.profile, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ApiResponseService<Optional<User>> profile(Principal securityPrincipal) {

        Optional<User> foundUser = this.userRepo.findByEmail(securityPrincipal.getName());

        return new ApiResponseService<>(
                HttpStatus.ACCEPTED,
                foundUser
        );
    }
}
