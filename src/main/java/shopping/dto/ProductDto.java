package shopping.dto;

import shopping.entity.Product;

public class ProductDto {
    private String name;
    private int price;
    private String imageUrl;

    public ProductDto() {}

    public ProductDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getName(), product.getPrice(), product.getImage().getValue());
    }
}
