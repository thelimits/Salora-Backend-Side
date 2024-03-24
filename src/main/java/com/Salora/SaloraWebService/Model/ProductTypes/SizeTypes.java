package com.Salora.SaloraWebService.Model.ProductTypes;

import com.Salora.SaloraWebService.Model.Enums.SizeProduct.ClothingSize;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.OneSize;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.SneakersSize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SizeTypes {
    private ClothingSize clothingSize;
    private SneakersSize sneakersSize;
    private OneSize oneSize;
}
