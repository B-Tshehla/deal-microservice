package com.enfint.deal.model;

import com.enfint.deal.dataEnum.CreditStatus;
import com.enfint.deal.dto.PaymentScheduleElement;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;


import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "credit")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {
    @Id
    @SequenceGenerator(name = "credit_sequence", sequenceName = "credit_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_sequence")

    private Long id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal rate;
    @Column(name = "full_loan_cost")
    private BigDecimal flc;
    @Type(type = "jsonb")
    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private List<PaymentScheduleElement> paymentSchedule;
    @Column(name = "is_insurance_enabled")
    private Boolean isInsuranceEnabled;
    @Column(name = "is_salary_client")
    private Boolean isSalaryClient;
    @Type(type = "jsonb")
    @Column(name = "credit_status", columnDefinition = "jsonb")
    private CreditStatus creditStatus;

    @OneToOne(mappedBy = "credit")
    private Application application;
}
