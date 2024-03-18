package com.Salora.SaloraWebService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="ProductCategory")
public class ProductCategory {
    @Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", type = com.Salora.SaloraWebService.Utils.UUIDStringGenerator.class)
    private String id;

    private String vendor;

    @OneToMany(mappedBy = "productCategory", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductCategoryType> categoryTypes;
}
