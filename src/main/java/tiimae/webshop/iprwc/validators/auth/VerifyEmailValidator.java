package tiimae.webshop.iprwc.validators.auth;

import java.util.Optional;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.constants.VerifyTokenEnum;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.VerifyToken;
import tiimae.webshop.iprwc.validators.Validator;

@Component
public class VerifyEmailValidator extends Validator {

    public String validateIfUserAleadyHasBeenVerified(Optional<User> user) {
        if (user.isEmpty()) {
            return "The user you are trying to verify was not found";
        }

        if (user.get().getVerified()) {
            return "This user is already verified, cant send a verify token";
        }

        return null;
    }

    public String validateVerifyToken(Optional<User> user, Optional<VerifyToken> verifyToken) {
        if (user.isEmpty()) {
            return "Something went wrong, please try again in a moment";
        }

        if (verifyToken.isEmpty() || !verifyToken.get().getType().equals(VerifyTokenEnum.VERIFY.toString())) {
            return "This token is invalid";
        }

        return null;
    }
}
