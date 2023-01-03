package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;

    private Boolean verified;
    private Boolean resetRequired;

    private UUID[] orderIds;
    private UUID[] roleIds;
    private UUID[] userAddressIds;

}
