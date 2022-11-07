package com.enfint.deal.controller;

import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import com.enfint.deal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final ApplicationService applicationService;

    @PostMapping("/application")
    public List<LoanOfferDTO> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest){
        return applicationService.getListOfLoanOffers(loanApplicationRequest);
    }

    @PutMapping("/offer")
    public void updateApplication(@RequestBody LoanOfferDTO loanOffer){
        applicationService.updateApplication(loanOffer);
    }

    @PutMapping("calculate/{applicationId}")
    public void updateCredit(@PathVariable("applicationId") Long applicationId,
                             @RequestBody ScoringDataDTO scoringData){

        applicationService.creditCreation(applicationId,scoringData);

    }

}
