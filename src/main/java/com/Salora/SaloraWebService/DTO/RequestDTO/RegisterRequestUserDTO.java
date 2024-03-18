package com.Salora.SaloraWebService.DTO.RequestDTO;

import com.Salora.SaloraWebService.Model.Enums.GenderType;
import com.Salora.SaloraWebService.Security.RolePermission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequestUserDTO {
    private String name;
    private GenderType gender;
    private Date birthDate;
    private String email;
    private String password;
    private RolePermission role;
}
