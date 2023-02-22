package tiimae.webshop.iprwc.DTO;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressDTO {

    private String street;
    private Integer houseNumber;
    private String addition;
    private String zipcode;
    private String city;
    private String country;
    private String type;
    private UUID userId;

}
