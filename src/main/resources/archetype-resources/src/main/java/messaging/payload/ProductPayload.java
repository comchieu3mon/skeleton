#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.messaging.payload;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @author duccaom
 * @version 1.0
 * @since 2022/11/04
 */
@Data
public class OrderlinePayload {

  @NotNull
  @Size(min = 1, max = 32)
  private String productID;

  @NotNull
  private Integer quantity;

  @NotNull
  private BigDecimal initialCost;

  @NotNull
  private Long contractId;

  @NotNull
  private Long deliveryOptionTypeID;

  @NotNull
  @Size(min = 1, max = 32)
  private String warehouseId;
}
