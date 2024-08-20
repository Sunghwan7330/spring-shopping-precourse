package shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.ProductManager;
import shopping.common.ApiResponse;
import shopping.dto.AddProductRequestDto;
import shopping.dto.ModifyProductRequestDto;
import shopping.dto.ProductDto;
import org.springframework.web.bind.annotation.*;
import shopping.entity.Product;

@RestController
public class ProductController {
    private final ProductManager productManager;

    public ProductController(ProductManager productManager) {
        this.productManager = productManager;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ApiResponse<ProductDto>> addProduct(@RequestBody AddProductRequestDto request) {
        ApiResponse res = productManager.addProduct(request.toProductDto());
        return ResponseEntity.status(200).body(res);
    }

    @PutMapping("/api/products")
    public ResponseEntity<ApiResponse<ProductDto>> modifyProduct(@RequestBody ModifyProductRequestDto request) {
        return ResponseEntity.ok(productManager.modifyProduct(request));
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductDto> getData(@PathVariable("productId") Long productId) {
        return ResponseEntity.status(200).body(productManager.getData(productId));
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Boolean> deleteData(@PathVariable("productId") Long productId) {
        try {
            productManager.deleteData(productId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(200).body(false);
        }
        return ResponseEntity.status(200).body(true);
    }
}
