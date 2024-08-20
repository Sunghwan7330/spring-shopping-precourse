package shopping;

import org.springframework.stereotype.Service;
import shopping.common.ApiResponse;
import shopping.dto.ModifyProductRequestDto;
import shopping.dto.ProductDto;
import shopping.entity.Product;
import shopping.repository.ProductRepository;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductManager {
    private final ProductRepository productRepository;
    private AtomicLong nextId;

    public ProductManager(ProductRepository productRepository) {
        nextId = new AtomicLong();
        this.productRepository = productRepository;
    }

    public ApiResponse addProduct(ProductDto productDto) {
        if(productRepository.findByName(productDto.getName()).isPresent()) {
            throw new RuntimeException("동일한 상품명이 이미 존재합니다.");
        }

        Product product;
        product = new Product(
                nextId.incrementAndGet(),
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );

        productRepository.save(product);
        return ApiResponse.ofSuccess(ProductDto.of(product));
    }

    public ApiResponse modifyProduct(ModifyProductRequestDto request) {
        Product product = productRepository.findById(request.productId()).orElseThrow(()->new RuntimeException("상품을 찾지 못했습니다."));
        product.update(request);

        return ApiResponse.ofSuccess(ProductDto.of(product));
    }

    public ProductDto getData(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("상품이 존재하지 않습니다."));
        return ProductDto.of(product);
    }

    public void deleteData(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }
}
