package tiimae.webshop.iprwc.service.auth;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

@Service
@AllArgsConstructor
public class ProfileService {

    private UserDAO userDAO;

    public ApiResponseService<Optional<User>> getProfile(Principal securityPrincipal) throws EntryNotFoundException {
        Optional<User> foundUser = Optional.ofNullable(this.userDAO.getByEmail(securityPrincipal.getName()));

        return new ApiResponseService<>(
                HttpStatus.ACCEPTED,
                foundUser
        );
    }

}
