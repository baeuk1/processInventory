package inventory.controller;

import cm.dto.api.ResponseDto;
import inventory.dto.VariableRequest;
import inventory.service.InventoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory/linkage")
public class InventoryController {

    private final InventoryService inventoryService;

    @ApiOperation("재고차감 수신")
    @PostMapping("/reduceInventory")
    public ResponseDto<?> reduceInventory(
            @ApiParam("재고차감 파라미터") @RequestBody @Valid final InventoryRequest request) {

    	inventoryService.reduceInventory(request.toEntity());
        return new ResponseDto<>(HttpStatus.OK.value(), "Success");
    }
}