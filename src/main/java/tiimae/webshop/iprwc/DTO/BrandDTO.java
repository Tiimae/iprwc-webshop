package tiimae.webshop.iprwc.DTO;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandDTO {

    @NotNull(message = "Brand name can't be empty!")
    private String brandName;

    @NotNull(message = "Website url can't be empty!")
    private String webPage;
    private String logo;

    private String[] productIds;

}
