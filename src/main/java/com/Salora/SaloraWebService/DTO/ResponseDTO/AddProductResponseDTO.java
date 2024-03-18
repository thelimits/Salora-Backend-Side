package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.Salora.SaloraWebService.DTO.ProductDTO.ProductCategoryDTO;
import com.Salora.SaloraWebService.Model.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddProductResponseDTO {
    private String id;
    private String productName;
    private String sku;
    private String description;
    private String imageUrl;
    private ProductCategory productCategory;
}
