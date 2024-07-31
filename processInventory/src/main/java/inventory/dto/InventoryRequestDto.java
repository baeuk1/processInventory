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

    @ApiParam(value="상품ID", required = true)
    @NotBlank(message = "상품ID(skuCode)는 필수 값입니다.")
    private String skuCode;

    @ApiParam(value="재고반영수량", required = true)
    @NotNull(message = "재고반영수량(quantity)는 필수 값입니다.")
    private Integer quantity;

    @ApiParam(value="재고반영유형코드", required = true)
    @NotBlank(message = "재고반영유형코드(typeCode)는 필수 값입니다.")
    private String typeCode;

    public InventoryQuantity toEntity() {
        return InventoryQuantity.builder()
                        .salestrNo(this.salestrNo)
                        .skuCode(this.skuCode)
                        .quantity(this.quantity)
                        .typeCode(this.typeCode)
                        .build();
    }
}
