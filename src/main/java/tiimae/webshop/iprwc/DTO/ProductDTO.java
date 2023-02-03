package tiimae.webshop.iprwc.DTO;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

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
