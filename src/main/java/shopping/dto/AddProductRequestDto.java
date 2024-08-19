package shopping.dto;

import jakarta.validation.constraints.NotNull;

public class AddProductRequestDto {
    @NotNull(message = "name is null.")
    private String name;
    @NotNull(message = "Price is null.")
    private int price;
    @NotNull(message = "ImageUrl is null.")
    private String imageUrl;

    public ProductDto toProductDto() {
        return new ProductDto(name, price, imageUrl);
    }

    public @NotNull(message = "name is null.") String getName() {
        return name;
    }

    @NotNull(message = "Price is null.")
    public int getPrice() {
        return price;
    }

    public @NotNull(message = "ImageUrl is null.") String getImageUrl() {
        return imageUrl;
    }
}
