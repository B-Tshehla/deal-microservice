package com.enfint.deal.service;

import com.enfint.deal.dataEnum.ChangeType;
import com.enfint.deal.dataEnum.Status;
import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.Passport;
import com.enfint.deal.fiegnClient.ConveyorClient;
import com.enfint.deal.model.Application;
import com.enfint.deal.model.Client;
import com.enfint.deal.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.enfint.deal.dataEnum.ChangeType.*;
import static com.enfint.deal.dataEnum.Status.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
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

    };

    @Transactional
    public void updateApplication(LoanOfferDTO loanOffer) {
        Application application = applicationRepository
                .findById(loanOffer.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application does not exist"));
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

}
