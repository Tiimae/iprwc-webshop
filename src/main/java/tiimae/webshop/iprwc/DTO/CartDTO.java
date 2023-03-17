package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {

    private String id;
    private Long quantity;
    private String userId;

    private String productId;

}
