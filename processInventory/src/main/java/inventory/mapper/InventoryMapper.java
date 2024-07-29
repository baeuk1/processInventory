package inventory.mapper;

import inventory.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface InventoryMapper {

    Optional<SaleStore> findSaleStore(@Param("salestrNo") String salestrNo);

    Optional<StoreItem> findStoreItem(@Param("strCd") String strCd, @Param("offlineItemId") String offlineItemId);

    void callCommInv(InventoryQuantity inventoryQuantity);
}
