package com.Salora.SaloraWebService.DTO.ProductDTO;

import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCategoryTypesDTO {
    private CategoryType type;
}
