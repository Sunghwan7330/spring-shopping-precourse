package shopping.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

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