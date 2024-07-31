package inventory.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class InventoryQuantity {

    private String salestrNo;     		// 영업점
    private String skuCode; 		// sku코드
    private Integer quantity;  // 재고반영수량(+ : 증가, - : 차감)
    private String typeCode; // 재고반영사유코드

    @Builder
    private InventoryQuantity(String salestrNo, String skuCode, Integer quantity, String typeCode) {
        this.salestrNo = salestrNo;
        this.skuCode = skuCode;
        this.quantity = quantity;
        this.typeCode = typeCode;
    }
}
