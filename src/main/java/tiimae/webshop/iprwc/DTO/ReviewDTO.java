package tiimae.webshop.iprwc.DTO;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private String description;
    private Integer stars;

    private String productId;

}
