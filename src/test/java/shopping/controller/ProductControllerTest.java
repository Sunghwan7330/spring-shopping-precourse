package shopping.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ProductController productController;

    @Autowired
    private RestClient.Builder clientBuilder;
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = clientBuilder.build();
    }

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
        String url = "http://localhost:" + port + "/shop/addProduct";
//        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";
        String requestBody = createRequest("productName", 3000, "http://test.com/test.jpg");

        ResponseEntity<Boolean> responseEntity =  restClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(Boolean.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).contains("true");
    }

    @Test
    @DisplayName("상품 중복 추가 실패")
    void failToAddProductCauseDuplicateProduct() {
        String url = "http://localhost:" + port + "/shop/addProduct";
        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";

        RestClient.RequestBodySpec bodySpec = restClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        bodySpec.retrieve().toEntity(Boolean.class);
        ResponseEntity<Boolean> responseEntity = bodySpec.retrieve().toEntity(Boolean.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).contains("false");
    }

    @Test
    @DisplayName("상품 조회")
    void getProduct() {
        String url = "http://localhost:" + port + "/api/products";
        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";

        restClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(Boolean.class);

        ResponseEntity<String> responseEntity =  restClient.get().uri(url + "?name=productName")
                .retrieve()
                .toEntity(String.class);

        String expect = "{\"id\":null,\"name\":\"productName\",\"image\":{\"value\":\"http://test.com/test.jpg\"}}";
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).isEqualTo(expect);
    }

    @Test
    @DisplayName("상품 실패 조회")
    void failToGetProductCauseNotFound() {
        String url = "http://localhost:" + port + "/api/products";

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> responseEntity =  restClient.get().uri(url + "?name=productName")
                    .retrieve()
                    .toEntity(String.class);

        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    @DisplayName("상품 제거")
    void deleteProduct() {
        String url = "http://localhost:" + port + "/shop/addProduct";   // TODO 변경 필요
        String requestBody = "{ \"name\": \"productName\", \"price\": 3000, \"imageUrl\": \"http://test.com/test.jpg\" }";

        restClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(Boolean.class);

        url = "http://localhost:" + port + "/api/products";
        ResponseEntity<Boolean> responseEntity =  restClient.delete().uri(url + "?name=productName")
                .retrieve()
                .toEntity(Boolean.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().toString()).contains("true");
    }

    @Test
    @DisplayName("상품 제거 실패")
    void failToDeleteProductCauseNotFound() {
        String url = "http://localhost:" + port + "/api/products";
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<Boolean> responseEntity =  restClient.delete().uri(url + "?name=productName")
                    .retrieve()
                    .toEntity(Boolean.class);

        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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