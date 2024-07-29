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
    private String offlineItemId; 		// sku코드
    private Integer inventoryQuantity;  // 재고반영수량(+ : 증가, - : 차감)
    private String inventoryReasonCode; // 재고반영사유코드

    @Builder
    private InventoryQuantity(String salestrNo, String offlineItemId,
                             Integer inventoryQuantity, String inventoryReasonCode
                             ) {
        this.salestrNo = salestrNo;
        this.offlineItemId = offlineItemId;
        this.inventoryQuantity = inventoryQuantity;
        this.inventoryReasonCode = inventoryReasonCode;
    }
}
