package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewDTO {

    private String description;
    private int stars;
    private UUID productId;

}
