package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    @NotNull(message = "First name can't be empty")
    private String firstName;
    private String middleName;
    @NotNull(message = "Last name can't be empty")
    private String lastName;
    @NotNull(message = "Email can't be empty")
    private String email;
    private String password;

    private Boolean verified;
    private Boolean resetRequired;

    private UUID[] orderIds;
    private String[] roleIds;
    private UUID[] userAddressIds;

}
