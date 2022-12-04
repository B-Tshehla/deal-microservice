package com.enfint.deal.dto;


import com.enfint.deal.dataEnum.CreditStatus;
import com.enfint.deal.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditModelDTO {
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal rate;
    private BigDecimal flc;
    private List<PaymentScheduleElement> paymentSchedule;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private CreditStatus creditStatus;

}
