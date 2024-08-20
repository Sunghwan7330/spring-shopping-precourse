package shopping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shopping.common.ApiResponse;
import shopping.dto.ModifyProductRequestDto;
import shopping.dto.ProductDto;
import shopping.entity.Product;
import shopping.repository.ProductRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ProductManagerTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    private ProductManager productManager;

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.jpg");
        ApiResponse res = productManager.addProduct(product);
        assertThat(res.getResult()).isTrue();
    }

    @Test
    @DisplayName("비정상 url 추가시 실패")
    void failToAddProductCauseInvalidUrl() {
        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.qwe");
        assertThatThrownBy(()->productManager.addProduct(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중복 상품 추가시 실패")
    void failToAddProductCauseDuplicateProduct() {
        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.jpg");
        given(productRepository.findByName(product.getName())).willReturn(any());
        assertThatThrownBy(()->productManager.addProduct(product)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct() {
        ModifyProductRequestDto request = new ModifyProductRequestDto(1L, "수정상품", 1000, "http://test.com/test.jpg");
        Product originProduct = new Product("name", 1000, "http://test.com/test.jpg");

        given(productRepository.findById(1L)).willReturn(Optional.of(originProduct));

        ApiResponse res = productManager.modifyProduct(request);
        assertThat(res.getResult()).isTrue();
        // TODO 상품명 체크 고려
//        ProductDto productDto = objectMapper.convertValue(response.getData(), ProductDto.class);
//        ProductDto productDto = objectMapper.convertValue(response.getData().toString(), ProductDto.class);
//       assertThat(productDto.getName()).isEqualTo("수정상품");

//        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.jpg");
//        ApiResponse res = productManager.addProduct(product);
//        assertThat(res.getResult()).isTrue();
    }

    @Test
    @DisplayName("상품 수정 실패")
    void failToUpdateProduct() {
        ModifyProductRequestDto request = new ModifyProductRequestDto(1L, "수정상품", 1000, "http://test.com/test.jpg");

        given(productRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productManager.modifyProduct(request)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("상품 제거")
    void deleteProduct() {
        Product product = new Product(1L, "name", 1000, "http://test.com/test.jpg");
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        productManager.deleteData(product.getId());
        verify(productRepository,times(1)).delete(product);
    }

    @Test
    @DisplayName("상품 제거 실패")
    void failToDeleteProduct() {
        Product product = new Product(1L, "name", 1000, "http://test.com/test.jpg");
        assertThatThrownBy(()-> productManager.deleteData(product.getId())).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("상품 조회")
    void getProducts() {
        Product product = new Product(1L, "name", 1000, "http://test.com/test.jpg");
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        ProductDto actual = productManager.getData(1L);

        assertThat(actual.getName()).isEqualTo("name");
        assertThat(actual.getPrice()).isEqualTo(1000);
        assertThat(actual.getImageUrl()).isEqualTo("http://test.com/test.jpg");
    }

    @Test
    @DisplayName("상품 조회 실패")
    void failToGetProducts() {
        Product product = new Product(1L, "name", 1000, "http://test.com/test.jpg");
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(()-> productManager.getData(1L)).isInstanceOf(RuntimeException.class);
    }

}