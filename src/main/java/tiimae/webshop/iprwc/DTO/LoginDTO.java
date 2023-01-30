package tiimae.webshop.iprwc.DTO;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotNull(message = "Must specify an email")
    private String email;

    @NotNull(message = "Must specify an password")
    private String password;

}
