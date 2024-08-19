package shopping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shopping.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductManagerTest {

    @Autowired
    private ProductManager productManager;

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.jpg");
        boolean res = productManager.addProduct(product);
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("비정상 url 추가시 실패")
    void failToAddProductCauseInvalidUrl() {
        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.qwe");
        boolean res = productManager.addProduct(product);
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("중복 상품 추가시 실패")
    void failToAddProductCauseDuplicateProduct() {
        ProductDto product = new ProductDto("name", 1000, "http://test.com/test.jpg");
        productManager.addProduct(product);
        boolean res = productManager.addProduct(product);

        assertThat(res).isFalse();
    }
}