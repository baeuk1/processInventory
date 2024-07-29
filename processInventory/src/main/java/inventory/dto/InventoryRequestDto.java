package inventory.dto;

import inventory.domain.InventoryQuantity;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class InventoryRequest {
    @ApiParam(value="영업점번호", required = true)
    @NotBlank(message = "영업점번호(salestrNo)는 필수 값입니다.")
    private String salestrNo;

    @ApiParam(value="오프라인상품ID", required = true)
    @NotBlank(message = "오프라인상품ID(offlineItemId)는 필수 값입니다.")
    private String offlineItemId;

    @ApiParam(value="재고반영수량", required = true)
    @NotNull(message = "재고반영수량(inventoryQuantity)는 필수 값입니다.")
    private Integer inventoryQuantity;

    @ApiParam(value="재고반영사유코드", required = true)
    @NotBlank(message = "재고반영사유코드(inventoryReasonCode)는 필수 값입니다.")
    private String inventoryReasonCode;

    public InventoryQuantity toEntity() {
        return InventoryQuantity.builder()
                        .salestrNo(this.salestrNo)
                        .offlineItemId(this.offlineItemId)
                        .inventoryQuantity(this.inventoryQuantity)
                        .inventoryReasonCode(this.inventoryReasonCode)
                        .build();
    }
}
