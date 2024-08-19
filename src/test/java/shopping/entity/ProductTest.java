package shopping.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ProductTest {

    @Test
    @DisplayName("상품 생성")
    void createProduct(){
        //given & when
        Product product = new Product(1L, "쿠키", 1500, "http://test.com/cookie.jpg");

        //then
        assertThat(product.getName()).isEqualTo("쿠키");
    }

    @Test
    @DisplayName("상품 생성 실패 - 이미지 URL 형식 다름")
    void createProductUrlFail(){
        assertThatThrownBy(()-> new Product(1L, "쿠키", 1500, "http://test.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("상품 생성 - url 테스트")
    @ValueSource(strings = {"http://test.com/cookie.jpg", "http://test.com/cookie.jpeg", "http://test.com/cookie.png", "http://test.com/cookie.gif"
    ,"https://test.com/cookie.jpg", "https://test.com/cookie.jpeg", "https://test.com/cookie.png", "https://test.com/cookie.gif"})
    void createProductUrl(String imageUrl){
        Product product = new Product(1L, "쿠키", 1500, imageUrl);

        assertThat(product.getImage().getValue()).isEqualTo(imageUrl);
    }
}