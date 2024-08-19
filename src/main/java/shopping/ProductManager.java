package shopping;

import org.springframework.stereotype.Service;
import shopping.dto.ModifyProductRequestDto;
import shopping.dto.ProductDto;
import shopping.entity.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductManager {
    private Map<Long, Product> productMap;
    private AtomicLong nextId;

    public ProductManager() {
        productMap = new HashMap<>();
        nextId = new AtomicLong();
    }

    public boolean addProduct(ProductDto productDto) {
        if (isExistName(productDto.getName())) {
            return false;
        }

        Product product;
        try {
            product = new Product(
                    nextId.incrementAndGet(),
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

    public ProductDto modifyProduct(ModifyProductRequestDto request) {
        if(!isExistId(request.productId())){
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        Product product = productMap.get(request.productId());
        product.update(request);

        return ProductDto.of(product);
    }

    private boolean isExistName(String name) {
        return productMap.values().stream().anyMatch(product -> product.getName().equals(name));
    }

    private boolean isExistId(Long id){
        return productMap.values().stream().anyMatch(product -> product.getId().equals(id));
    }
}
