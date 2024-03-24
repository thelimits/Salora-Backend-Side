package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.Salora.SaloraWebService.Model.Enums.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserAdditionalDetails {
    private AddressType addressType;

    private String firstName;

    private String middleName;

    private String lastName;

    private String numberPhone;

    private String otherNumber;

    private String address;

    private String province;

    private String city;

    private String district;

    private String subDistrict;

    private String postCode;
}
