package inventory.service;

import inventory.domain.InventoryQuantity;
import inventory.domain.SaleStore;
import inventory.domain.StoreItem;
import inventory.mapper.InventoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryMapper inventoryMapper;

    @Transactional
    public void reflectInventory(InventoryQuantity inventoryQuantity) {

        // 1. 영업점 확인
        SaleStore saleStore = getSaleStore(inventoryQuantity.getSalestrNo());
        
        // 2. 상품ID 확인
        StoreItem storeItem = getStoreItem(saleStore.getSalestrNo(), inventoryQuantity.getSkuCode());
        
        // 3. 현재재고수량 조회/검증 (optional)
        int curInvQty = getCurrentInventoryQuantity(saleStore.getSalestrNo(), inventoryQuantity);
        
        
        // 수불차감 프로시저 호출
        inventoryMapper.callCommInv(inventoryQuantity);
    }

    private StoreItem getStoreItem(String salestrNo, String skuCode) {
        return inventoryMapper.findStoreItem(salestrNo, skuCode)
                .orElseThrow(() -> new RuntimeException("영업점상품마스터에 존재하지 않는 상품입니다 영업점코드 : [" + salestrNo + "], 상품ID : [" + skuCode + "]"));
    }

    private SaleStore getSaleStore(String salestrNo) {
        return inventoryMapper.findSaleStore(salestrNo)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 영업점 번호입니다 [" + salestrNo + "]"));
    }
    
    private void getCurrentInventoryQuantity(InventoryQuantity inventoryQuantity) {
    	int curInvQty = inventoryMapper.getCurrentInventoryQuantity(inventoryQuantity.getSalestrNo(), inventoryQuantity.getSkuCode());
    	if(isPreventMinusQuantityByTypeCode(inventoryQuantity.getTypeCode())) {
        	if(curInvQty - inventoryQuantity.getQuantity() < 0) {
        		new RuntimeException("차감하고자하는 수량보다 현재 재고수량이 적습니다. 현재재고수량 [" + curInvQty + "], 차감시도수량 : " + inventoryQuantity.getQuantity());
        	}
        }
    }
    
    private boolean isPreventMinusQuantityByTypeCode(String typeCode) {
    	return "Y".equals(inventoryMapper.getPreventMinusQuantityByTypeCode(typeCode));
    }

}