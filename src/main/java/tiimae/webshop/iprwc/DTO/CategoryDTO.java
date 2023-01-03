package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryDTO {

    String categoryName;
    UUID[] productIds;

}
