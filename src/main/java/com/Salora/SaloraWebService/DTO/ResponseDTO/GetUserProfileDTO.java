package com.Salora.SaloraWebService.DTO.ResponseDTO;

import com.Salora.SaloraWebService.Model.Enums.GenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserProfileDTO {
    private String name;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
