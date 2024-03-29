package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDTO {

    @NotNull(message = "Must specify an email")
    private String email;

    @NotNull(message = "Must specify an password")
    private String password;

}
