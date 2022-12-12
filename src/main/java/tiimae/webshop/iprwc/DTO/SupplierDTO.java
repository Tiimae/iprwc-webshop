package tiimae.webshop.iprwc.DTO;

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

    private String[] productIds;

}
