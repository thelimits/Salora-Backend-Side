package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.Salora.SaloraWebService.DTO.ProductDTO.ProductDTO;
import com.Salora.SaloraWebService.Model.Product;
import com.Salora.SaloraWebService.Utils.ProductMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductResponseDTO {
    private int number;
    private long totalElements;
    private int totalPages;
    private Pageable nextPageable;
    private Pageable previousPageable;
    private List<ProductDTO> data;

    public GetProductResponseDTO(Page<Product> customerPage,  ProductMapper productMapper) {
        this.number = customerPage.getNumber();
        this.totalElements = customerPage.getTotalElements();
        this.totalPages = customerPage.getTotalPages() - 1;
        this.nextPageable = customerPage.getPageable().next();
        this.previousPageable = customerPage.getPageable().previousOrFirst();
        this.data = productMapper.mapObjects(customerPage.getContent(), ProductDTO.class);
    }
}
