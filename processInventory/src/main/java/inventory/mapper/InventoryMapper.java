package inventory.mapper;

import inventory.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface InventoryMapper {

    Optional<SaleStore> findSaleStore(@Param("salestrNo") String salestrNo);

    Optional<StoreItem> findStoreItem(@Param("salestrNo") String salestrNo, @Param("skuCode") String skuCode);
    
    int getCurrentInventoryQuantity(@Param("salestrNo") String salestrNo, @Param("skuCode") String skuCode);
    
    String getPreventMinusQuantityByTypeCode(@Param("typeCode") String typeCode);

    void callCommInv(InventoryQuantity inventoryQuantity);
}
