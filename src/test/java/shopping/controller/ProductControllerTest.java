package shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import shopping.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;

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
        url = "http://localhost:" + port;
        restClient = clientBuilder.build();
    }

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
//        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";
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
                .uri(url+"/shop/modifyProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(ProductDto.class);

        assertThat(response.getBody().getPrice()).isEqualTo(1500);
    }

    private ResponseEntity<Boolean> restClientAddProduct(String requestBody) {
        return restClient.post()
                .uri(url + "/shop/addProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(Boolean.class);
    }

    private String createRequest(String name, int price, String imageUrl){ //동작이 제대로 안되는 듯
        return STR."""
                {
                    "name": "\{name}",
                    "price": \{price},
                    "imageUrl": "\{imageUrl}"
                  }
                """;
    }
}