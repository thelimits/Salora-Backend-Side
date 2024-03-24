package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.Salora.SaloraWebService.Model.Cart;
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
public class GetCartResponseDTO {
    private String id;
    private String productName;
    private String description;
    private Double price;
    private String imageUrl;
    private Set<Cart> carts;
    private Set<ProductAttribute> attributes;
}
