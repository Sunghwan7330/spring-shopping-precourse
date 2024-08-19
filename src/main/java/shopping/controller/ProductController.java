package shopping.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.ProductManager;
import shopping.dto.AddProductRequestDto;

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
}
