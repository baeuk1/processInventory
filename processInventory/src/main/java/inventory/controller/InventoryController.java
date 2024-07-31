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

    @ApiOperation("재고변동 수신")
    @PostMapping("/reflectInventory")
    public ResponseDto<?> reflectInventory(
            @ApiParam("재고변동 파라미터") @RequestBody @Valid final InventoryRequest request) {

    	try {
    		inventoryService.reflectInventory(request.toEntity());
    	} catch(RuntimeException e) {
    		return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    	}
    	
        return new ResponseDto<>(HttpStatus.OK.value(), "Success");
    }
}