package com.enfint.deal.service;


import com.enfint.deal.dto.*;
import com.enfint.deal.exception.RecordNotFoundException;
import com.enfint.deal.fiegnClient.ConveyorClient;
import com.enfint.deal.model.Application;
import com.enfint.deal.model.Client;
import com.enfint.deal.model.Credit;
import com.enfint.deal.repository.ApplicationRepository;
import com.enfint.deal.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.enfint.deal.dataEnum.ChangeType.*;
import static com.enfint.deal.dataEnum.CreditStatus.*;
import static com.enfint.deal.dataEnum.Status.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final ConveyorClient conveyorClient;
    public List<LoanOfferDTO> getListOfLoanOffers(LoanApplicationRequestDTO loanApplicationRequest){

        Client client = new Client();
        Passport passport = new Passport();
        Application application = new Application();

        client.setFirstName(loanApplicationRequest.getFirstName());
        client.setLastName(loanApplicationRequest.getLastName());
        client.setMiddleName(loanApplicationRequest.getMiddleName());
        client.setEmail(loanApplicationRequest.getEmail());
        client.setBirthDate(loanApplicationRequest.getBirthdate());
        passport.setSeries(loanApplicationRequest.getPassportSeries());
        passport.setNumber(loanApplicationRequest.getPassportNumber());
        application.setCreationDate(LocalDate.now());
        application.setClient(client);
        client.setApplication(application);
        applicationRepository.save(application);

        return conveyorClient.getLoanOffers(loanApplicationRequest)
                .stream()
                .peek(offer -> offer
                .setApplicationId(application.getId()))
                .collect(Collectors.toList());

    }

    @Transactional
    public void updateApplication(LoanOfferDTO loanOffer) {
        Application application = applicationRepository
                .findById(loanOffer.getApplicationId())
                .orElseThrow(() ->  new RecordNotFoundException("The record not found"));
        application.setStatus(PREAPPROVAL);
        application.setStatusHistory(List.of(
                        new ApplicationStatusHistoryDTO(
                        PREAPPROVAL,
                        LocalDate.now(),
                        UPDATED
                        )
                )
        );
        application.setAppliedOffer(loanOffer);
        applicationRepository.save(application);
    }

    @Transactional
    public void creditCreation(Long applicationId, ScoringDataDTO scoringData){
        Credit credit = new Credit();
        Passport passport = new Passport();
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RecordNotFoundException("The record not found"));

        CreditDTO conveyorCredit = conveyorClient.getCredit(scoringData);
        Client client = application.getClient();
        client.setDependentNumber(scoringData.getDependentAmount());
        client.setAccount(scoringData.getAccount());
        client.setGender(scoringData.getGender());
        client.setMaritalStatus(scoringData.getMaritalStatus());
        client.setEmployment(scoringData.getEmployment());
        passport.setSeries(scoringData.getPassportSeries());
        passport.setNumber(scoringData.getPassportNumber());
        passport.setIssue_date(scoringData.getPassportIssueDate());
        passport.setIssue_branch(scoringData.getPassportIssueBranch());
        client.setPassport(passport);
        credit.setAmount(conveyorCredit.getAmount());
        credit.setTerm(conveyorCredit.getTerm());
        credit.setRate(conveyorCredit.getRate());
        credit.setFlc(conveyorCredit.getPsk());
        credit.setIsInsuranceEnabled(conveyorCredit.getIsInsuranceEnabled());
        credit.setIsSalaryClient(conveyorCredit.getIsSalaryClient());
        credit.setPaymentSchedule(conveyorCredit.getPaymentSchedule());
        credit.setCreditStatus(CALCULATED);
        application.setCredit(credit);
        credit.setApplication(application);
        applicationRepository.save(application);
    }
}
