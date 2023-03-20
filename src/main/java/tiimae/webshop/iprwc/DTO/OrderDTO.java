package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class OrderDTO {
    
    private String orderId;
    private Date orderDate;
    private String userId;
    private String[] productIds;
    private String[] addressIds;

}
