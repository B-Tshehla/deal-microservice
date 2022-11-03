package com.enfint.deal.fiegnClient;

import com.enfint.deal.dto.CreditDTO;
import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "conveyorClient",url = "http://localhost:8081/conveyor" )
public interface ConveyorClient {
    @PostMapping("/offers")
    List<LoanOfferDTO> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PostMapping("/calculation")
    CreditDTO getCredit(@RequestBody ScoringDataDTO scoringData);
}
