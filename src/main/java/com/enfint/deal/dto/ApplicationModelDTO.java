package com.enfint.deal.dto;

import com.enfint.deal.dataEnum.Status;
import com.enfint.deal.model.Client;
import com.enfint.deal.model.Credit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationModelDTO {
    private Long id;
    private Status status;
    private LocalDate creationDate;
    private LoanOfferDTO appliedOffer;
    private LocalDate signDate;
    private int sesCode;
    private List<ApplicationStatusHistoryDTO> statusHistory;
    private ClientModelDTO client;

}
