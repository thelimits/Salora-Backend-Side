package com.Salora.SaloraWebService.Model;

import com.Salora.SaloraWebService.Model.Enums.AddressType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="UserDetails")
public class UserAdditionalDetails {
    @Id
    @GeneratedValue(generator = "uuidString")
    @GenericGenerator(name = "uuidString", type = com.Salora.SaloraWebService.Utils.UUIDStringGenerator.class)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    private String lastName;

    @Column(nullable = false)
    private String numberPhone;

    private String otherNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String subDistrict;

    @Column(nullable = false)
    private String postCode;
}
