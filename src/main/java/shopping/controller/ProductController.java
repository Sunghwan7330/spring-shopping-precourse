package shopping.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shopping.ProductManager;
import shopping.dto.AddProductRequestDto;
import shopping.entity.Product;

@RestController
public class ProductController {
    private final ProductManager productManager;

    public ProductController(ProductManager productManager) {
        this.productManager = productManager;
    }

    @PostMapping("/shop/addProduct")
    public ResponseEntity<Boolean> addProduct(@RequestBody AddProductRequestDto request) {
        boolean res = productManager.addProduct(request.toProductDto());
        return ResponseEntity.status(200).body(res);
    }
    @GetMapping("/api/products")
    public ResponseEntity<Product> getData(@RequestParam(name = "name", defaultValue = "") String name) {
        Product product = productManager.getExistName(name);

        if (product == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(product);
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
