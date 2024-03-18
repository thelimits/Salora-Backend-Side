package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductDetailsResponseDTO {
    private String productName;
    private String sku;
    private Double price;
    private Integer stock;
    private String description;
    private String longDescriptions;
    private String mediumDescriptions;
    private String imageName;
    private String imageUrl;
}
