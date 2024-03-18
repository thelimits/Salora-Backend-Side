package com.Salora.SaloraWebService.Config.FirebaseStorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Data
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseConfig {
    private String serviceAccountName;

    private String bucketName;

    private String imageUrl;

    @PostConstruct
    public void initialize(){
        try {
            ClassPathResource serviceAccount = new ClassPathResource(serviceAccountName);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(bucketName)
                    .build();
            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
