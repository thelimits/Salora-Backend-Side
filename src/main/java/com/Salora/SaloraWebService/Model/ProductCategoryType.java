package com.Salora.SaloraWebService.Model;

import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="ProductCategoryType")
public class ProductCategoryType {
    @Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", type = com.Salora.SaloraWebService.Utils.UUIDStringGenerator.class)
    private String id;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id")
    @JsonIgnore
    private ProductCategory productCategory;
}
