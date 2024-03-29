package com.Salora.SaloraWebService.DTO.RequestDTO;

import com.Salora.SaloraWebService.Model.ProductAttribute;
import com.Salora.SaloraWebService.Model.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddProductDTO {
    private String productName;
    private String sku;
    private Double price;
    private String description;
    private String longDescriptions;
    private String mediumDescriptions;
    private ProductCategory productCategory;
    private Set<ProductAttribute> attributes;
}
