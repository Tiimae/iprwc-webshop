package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductDTO {

    private String name;
    private String description;
    private Float price;

    private UUID brandId;
    private UUID categoryId;
    private UUID supplierId;

}
