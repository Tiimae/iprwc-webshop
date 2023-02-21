package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandDTO {

    private String brandName;
    private String webPage;
    private String logo;

    private String[] productIds;

}
