package inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreItem {
    private String buyFrmCd;
    private BigDecimal sellprc;
    private BigDecimal splprc;

}

