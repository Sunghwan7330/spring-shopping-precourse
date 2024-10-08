package shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import shopping.dto.ProductDto;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    @LocalServerPort
    private int port;
    private String url;

    @Autowired
    private RestClient.Builder clientBuilder;
    private RestClient restClient;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port+"/api/products";
        restClient = clientBuilder.build();
    }

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
        String requestBody = createRequest("productName", 3000, "http://test.com/test.jpg");

        ResponseEntity<Boolean> responseEntity = restClientAddProduct(requestBody);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).contains("true");
    }

    @Test
    @DisplayName("상품 중복 추가 실패")
    void failToAddProductCauseDuplicateProduct() {
        String requestBody = createRequest("productName", 3000, "http://test.com/test.jpg");

        restClientAddProduct(requestBody);
        ResponseEntity<Boolean> responseEntity = restClientAddProduct(requestBody);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).contains("false");
    }

    @Test
    @DisplayName("상품 수정")
    void modifyProduct() {
        String request = "{ \"productId\": 1, \"price\": 1500 }";
        restClientAddProduct(createRequest("productName", 3000, "http://test.com/test.jpg"));

        ResponseEntity<ProductDto> response = restClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(ProductDto.class);

        assertThat(response.getBody().getPrice()).isEqualTo(1500);
    }

    @Test
    @DisplayName("상품 조회")
    void getProduct() {
        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";

        restClientAddProduct(requestBody);

        // 예시로 사용할 productId
        Long productId = 1L;
        ResponseEntity<ProductDto> responseEntity =  restClient.get()
                .uri(url + "/{productId}", productId)
                .retrieve()
                .toEntity(ProductDto.class);

        ProductDto expect = new ProductDto("productName", 3000, "http://test.com/test.jpg");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo(expect.getName());
        assertThat(responseEntity.getBody().getPrice()).isEqualTo(expect.getPrice());
        assertThat(responseEntity.getBody().getImageUrl()).isEqualTo(expect.getImageUrl());
    }

    @Test
    @DisplayName("상품 제거")
    void deleteProduct() {
        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";

        restClientAddProduct(requestBody);

        Long productId = 1L;
        ResponseEntity<Boolean> responseEntity =  restClient.delete()
                .uri(url+"/{productId}", productId)
                .retrieve()
                .toEntity(Boolean.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).contains("true");
    }

    @Test
    @DisplayName("상품 제거 실패")
    void failToDeleteProductCauseNotFound() {
        Long productId = 1L;
        ResponseEntity<Boolean> responseEntity =  restClient.delete()
                .uri(url+"/{productId}", productId)
                .retrieve()
                .toEntity(Boolean.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).contains("false");
    }

    private ResponseEntity<Boolean> restClientAddProduct(String requestBody) {
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(Boolean.class);
    }
  
    private String createRequest(String name, int price, String imageUrl){
        return STR."""
                {
                    "name": "\{name}",
                    "price": \{price},
                    "imageUrl": "\{imageUrl}"
                  }
                """;
    }
}