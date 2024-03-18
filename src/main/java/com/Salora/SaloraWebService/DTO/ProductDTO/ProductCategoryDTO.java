package com.Salora.SaloraWebService.DTO.ProductDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryDTO {
    private String vendor;
    private Set<ProductCategoryTypesDTO> categoryTypes;
}
