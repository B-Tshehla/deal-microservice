package com.enfint.deal.controller;

import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import com.enfint.deal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {
    private final ApplicationService applicationService;

    @PostMapping("/application")
    public List<LoanOfferDTO> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("******************** Getting Loan Offers ********************");
        log.info("loanApplicationRequest {} ", loanApplicationRequest);
        return applicationService.getListOfLoanOffers(loanApplicationRequest);
    }

    @PutMapping("/offer")
    public void updateApplication(@RequestBody LoanOfferDTO loanOffer) {
        log.info("******************** Updating Application ********************");
        log.info("loanOffer {} ", loanOffer);
        applicationService.updateApplication(loanOffer);
    }

    @PutMapping("calculate/{applicationId}")
    public void updateCredit(@PathVariable("applicationId") Long applicationId, @RequestBody ScoringDataDTO scoringData) {
        log.info("******************** Creating credit ********************");
        log.info("applicationId {} scoringData {}", applicationId, scoringData);
        applicationService.creditCreation(applicationId, scoringData);

    }

}
