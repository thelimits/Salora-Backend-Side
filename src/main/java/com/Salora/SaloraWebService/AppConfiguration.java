package com.Salora.SaloraWebService;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfiguration {
    @Value(("${restTemplate.timeout}"))
    private int TIMEOUT;

    @Value(("${spring.timeout-client}"))
    private int TIMEOUT_CLIENT;

    @Value(("${firebase.image-url}"))
    private String imageUrl;

    @Value(("${firebase.path-image}"))
    private  String imagePath;

    @Value(("${firebase.bucket-name}"))
    private String bucketName;

    @Value(("${firebase.base-image-url}"))
    private String baseImageUrl;
}
