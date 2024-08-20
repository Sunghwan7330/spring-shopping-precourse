package shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.ProductManager;
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
    public ResponseEntity<Boolean> addProduct(@RequestBody AddProductRequestDto request) {
        boolean res = productManager.addProduct(request.toProductDto());
        return ResponseEntity.status(200).body(res);
    }

    @PutMapping("/api/products")
    public ResponseEntity<ProductDto> modifyProduct(@RequestBody ModifyProductRequestDto request) {
        return ResponseEntity.ok(productManager.modifyProduct(request));
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductDto> getData(@PathVariable("productId") Long productId) {
        return ResponseEntity.status(200).body(productManager.getData(productId));
    }

    @DeleteMapping("/api/products")
    public ResponseEntity<Boolean> deleteData(@RequestParam(name = "name", defaultValue = "") String name) {
        Product product = productManager.getExistName(name);

        if (product == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(true);
    }
}
