package com.Salora.SaloraWebService.DTO.ProductDTO;

import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.Salora.SaloraWebService.Model.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private String id;
    private String productName;
    private String description;
    private Double price;
    private String imageUrl;
}
