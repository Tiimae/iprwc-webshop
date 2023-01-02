package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    private String orderId;
    private Date orderDate;
    private UUID userId;
    private UUID[] productIds;
    private UUID[] addressIds;

}
