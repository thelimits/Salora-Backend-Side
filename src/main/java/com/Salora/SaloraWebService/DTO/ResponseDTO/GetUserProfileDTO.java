package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.Salora.SaloraWebService.Model.Enums.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserProfileDTO {
    private String name;
    private String email;
    private Date birthDate;
    private String password;
}
