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
    public void reduceInventory(InventoryQuantity inventoryQuantity) {

        // 1. 영업점 검증
        SaleStore saleStore = getSaleStore(inventoryQuantity.getSalestrNo());
        
        // 2. 상품ID 검증
        StoreItem storeItem = getStoreItem(saleStore.getStrCd(), inventoryQuantity.getOfflineItemId());
        
        // todo : 수량 or 사유 검증 추가 가능
        
        // 수불차감 프로시저 호출
        inventoryMapper.callCommInv(inventoryQuantity);
    }

    private StoreItem getStoreItem(String strCd, String offlineItemId) {
        return inventoryMapper.findStoreItem(strCd, offlineItemId)
                .orElseThrow(() -> new RuntimeException("점포상품마스터에 존재하지 않는 상품입니다 점포코드 : [" + strCd + "], 오프라인상품ID : [" + offlineItemId + "]"));
    }

    private SaleStore getSaleStore(String salestrNo) {
        return inventoryMapper.findSaleStore(salestrNo)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 영업점 번호입니다 [" + salestrNo + "]"));
    }

}