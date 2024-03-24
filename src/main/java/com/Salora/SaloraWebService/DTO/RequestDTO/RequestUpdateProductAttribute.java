package com.Salora.SaloraWebService.DTO.RequestDTO;

import com.Salora.SaloraWebService.Model.ProductAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestUpdateProductAttribute {
    private Set<ProductAttribute> attributes;
}
