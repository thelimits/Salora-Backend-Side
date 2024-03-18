package com.Salora.SaloraWebService.DTO.RequestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequestUserDTO {
    private String email;
    private String password;
}
