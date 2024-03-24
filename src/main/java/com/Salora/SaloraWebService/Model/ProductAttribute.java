package com.Salora.SaloraWebService.Model;

import com.Salora.SaloraWebService.Model.Enums.SizeProduct.SizeDefine;
import com.Salora.SaloraWebService.Model.ProductTypes.SizeTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="ProductAttribute")
public class ProductAttribute{
    @Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", type = com.Salora.SaloraWebService.Utils.UUIDStringGenerator.class)
    private String id;

    @Enumerated(EnumType.STRING)
    private SizeDefine size;

    @Embedded
    private SizeTypes sizeType;

    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
}
