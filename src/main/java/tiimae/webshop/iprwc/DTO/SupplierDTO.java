package tiimae.webshop.iprwc.DTO;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

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
