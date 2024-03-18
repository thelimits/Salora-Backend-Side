package com.Salora.SaloraWebService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="Product")
public class Product extends DateAuditEntity {
    @Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", type = com.Salora.SaloraWebService.Utils.UUIDStringGenerator.class)
    private String id;

    private String productName;
    @Column(unique = true)
    private String sku;
    private Double price;
    private Integer stock;
    private String description;
    @Column(length = 150000)
    private String longDescriptions;
    @Column(length = 150000)
    private String mediumDescriptions;
    private String imageName;
    @Column(length = 1500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id", nullable = false)
    @JsonIgnore
    private ProductCategory productCategory;
}
