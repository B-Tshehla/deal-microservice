package com.enfint.deal.dto;

import com.enfint.deal.dataEnum.Gender;
import com.enfint.deal.dataEnum.MaritalStatus;
import com.enfint.deal.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientModelDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentNumber;
    private Passport passport;
    private EmploymentDTO employment;
    private String account;


}
