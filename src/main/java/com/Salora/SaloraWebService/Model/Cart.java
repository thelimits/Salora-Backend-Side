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
@Entity(name="Cart")
public class Cart extends DateAuditEntity{
    @Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", type = com.Salora.SaloraWebService.Utils.UUIDStringGenerator.class)
    private String id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "product_attribute_sku")
    private ProductAttribute productAttribute;
}
