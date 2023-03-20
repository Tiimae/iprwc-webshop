package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SupplierDTO {

    private String name;
    private String address;
    private String zipcode;
    private String city;
    private String country;

    private UUID[] productIds;

}
