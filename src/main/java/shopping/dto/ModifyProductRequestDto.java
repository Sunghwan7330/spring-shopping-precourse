package shopping.dto;

public record ModifyProductRequestDto(
        Long productId,
        String productName,
        int price,
        String imageUrl
){
}
