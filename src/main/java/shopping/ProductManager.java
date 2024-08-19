package shopping;

import org.springframework.stereotype.Service;
import shopping.dto.ProductDto;
import shopping.entity.Product;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductManager {
    private Map<Long, Product> productMap;

    public ProductManager() {
        productMap = new HashMap<>();
    }

    public Product getExistName(String name) {
        for (Product product : productMap.values()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    private boolean isExistName(String name) {
        for (Product product : productMap.values()) {
            if (product.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean addProduct(ProductDto productDto) {
        if (isExistName(productDto.getName())) {
            return false;
        }

        Product product;
        try {
            product = new Product(
                    productDto.getName(),
                    productDto.getPrice(),
                    productDto.getImageUrl()
            );
        } catch (IllegalArgumentException e) {
            return false;
        }
        productMap.put(product.getId(), product);
        return true;
    }
}
