package com.enfint.deal.controller;

import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    ApplicationService applicationService;

    @PostMapping("/application")
    public List<LoanOfferDTO> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest){
        return applicationService.getListOfLoanOffers(loanApplicationRequest);
    }
}
