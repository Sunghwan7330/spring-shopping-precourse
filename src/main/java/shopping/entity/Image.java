package shopping.entity;


import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class Image {
    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile( "^(https?:\\/\\/)?([\\w\\-]+\\.)+[\\w\\-]" +
            "+(\\/[\\w\\-\\.]+)*\\.(jpg|jpeg|png|gif)$", Pattern.CASE_INSENSITIVE);

    private String value;

    public Image() {
        throw new IllegalArgumentException("사용할 수 없는 생성자입니다.");
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
