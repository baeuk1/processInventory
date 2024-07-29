package com.ssg.api.rdc.inventory.linkage.ecms.mapper;

import com.ssg.api.rdc.inventory.linkage.ecms.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface InventoryMapper {

    Optional<SaleStore> findSaleStore(@Param("salestrNo") String salestrNo);

    Optional<StoreItem> findStoreItem(@Param("strCd") String strCd, @Param("offlineItemId") String offlineItemId);

    void saveInterface(VariableQuantity variableQuantity);

    List<VariableQuantity> findInterface(VariableQuantity variableQuantity);

    void updateProcessedInterface(VariableQuantity variableQuantity);

    void callCmsCommInv(InventoryReflection inventoryReflection);
}
