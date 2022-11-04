package com.enfint.deal.model;


import com.enfint.deal.dataEnum.Gender;
import com.enfint.deal.dataEnum.MaritalStatus;
import com.enfint.deal.dto.EmploymentDTO;
import com.enfint.deal.dto.Passport;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;


import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;


@Entity(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
public class Client {
    @Id
    @SequenceGenerator(name = "client_sequence", sequenceName = "clicent_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_sequence")

    private Long id;
    @Column(name = "last_name", columnDefinition = "VARCHAR(30)")
    private String lastName;
    @Column(name = "first_name",  columnDefinition = "VARCHAR(30)")
    private String firstName;
    @Column(name = "middle_name", columnDefinition = "VARCHAR(30)")
    private String middleName;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(70)")
    private String email;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    @Column(name = "dependent_number")
    private Integer dependentNumber;

    @Column(name = "passport", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Passport passport;
    @Column(name = "employment", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private EmploymentDTO employment;
    @Column(name = "account")
    private String account;

    @OneToOne(mappedBy = "client")
    Application application;
}

