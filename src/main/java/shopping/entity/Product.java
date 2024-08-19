package shopping.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    @Embedded
    private Image image;

    public Product(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.image = new Image(imageUrl);
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }
}
