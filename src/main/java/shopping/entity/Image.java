package shopping.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class Image {
    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile( "^(https?:\\/\\/)?([\\w\\-]+\\.)+[\\w\\-]" +
            "+(\\/[\\w\\-\\.]+)*\\.(jpg|jpeg|png|gif)$", Pattern.CASE_INSENSITIVE);

    @Column(name = "image_url")
    private String value;

    public Image() {
    }

    public Image(String imageUrl) {
        validate(imageUrl);
        this.value = imageUrl;
    }

    private void validate(String imageUrl) {
        if(!IMAGE_URL_PATTERN.matcher(imageUrl).matches()) {
            throw new IllegalArgumentException("Invalid image URL: " + imageUrl);
        }
    }

    public String getValue() {
        return value;
    }
}
