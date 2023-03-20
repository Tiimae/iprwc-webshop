package tiimae.webshop.iprwc.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DAO.VerifyTokenDAO;
import tiimae.webshop.iprwc.DAO.repo.RoleRepository;
import tiimae.webshop.iprwc.mapper.UserMapper;
import tiimae.webshop.iprwc.service.EmailService;
import tiimae.webshop.iprwc.service.EncryptionService;
import tiimae.webshop.iprwc.service.auth.LoginService;
import tiimae.webshop.iprwc.service.auth.ProfileService;
import tiimae.webshop.iprwc.validators.UserValidator;
import tiimae.webshop.iprwc.validators.auth.AuthValidator;

@RequestMapping(
        headers = "Accept=application/json",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Controller
public class AuthController {

    @Autowired protected RoleRepository roleRepository;
    @Autowired protected AuthenticationManager authManager;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected LoginService loginService;
    @Autowired protected EmailService emailService;
    @Autowired protected ProfileService profileService;
    @Autowired protected EncryptionService encryptionService;

    @Autowired protected AuthValidator authValidator;
    @Autowired protected UserValidator userValidator;

    @Autowired protected UserDAO userDAO;
    @Autowired protected VerifyTokenDAO verifyTokenDAO;

    @Autowired protected UserMapper userMapper;

    @Value("${jwt_secret}")
    protected String jwtSecret;
    @Value("${shared_secret}")
    protected String sharedSecret;
}
