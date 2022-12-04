package com.enfint.deal.service;





import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.EmailMessage;
import com.enfint.deal.dto.ScoringDataDTO;
import com.enfint.deal.dto.Passport;
import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.CreditDTO;
import com.enfint.deal.exception.recordNotFound.RecordNotFoundException;
import com.enfint.deal.fiegnClient.ConveyorClient;
import com.enfint.deal.kafka.MessageProducer;
import com.enfint.deal.model.Application;
import com.enfint.deal.model.Client;
import com.enfint.deal.model.Credit;
import com.enfint.deal.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.enfint.deal.dataEnum.ChangeType.AUTOMATIC;
import static com.enfint.deal.dataEnum.ChangeType.MANUAL;
import static com.enfint.deal.dataEnum.CreditStatus.CALCULATED;
import static com.enfint.deal.dataEnum.Status.PREAPPROVAL;
import static com.enfint.deal.dataEnum.Status.CC_APPROVED;
import static com.enfint.deal.dataEnum.Status.CC_DENIED;
import static com.enfint.deal.dataEnum.Theme.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ConveyorClient conveyorClient;
    private final MessageProducer messageProducer;
    private final DocumentService documentService;
    public List<LoanOfferDTO> getListOfLoanOffers(LoanApplicationRequestDTO loanApplicationRequest){

        log.info("******************** Passport information ********************");
        Passport passport = Passport
                .builder()
                .series(loanApplicationRequest.getPassportSeries())
                .number(loanApplicationRequest.getPassportNumber())
                .build();
        log.info("passport {} ", passport);
        log.info("******************** Client Information ********************");
        Client client = Client
                .builder()
                .firstName(loanApplicationRequest.getFirstName())
                .lastName(loanApplicationRequest.getLastName())
                .middleName(loanApplicationRequest.getMiddleName())
                .email(loanApplicationRequest.getEmail())
                .passport(passport)
                .birthDate(loanApplicationRequest.getBirthdate())
                .build();
        log.info("client {} ",client);
        log.info("******************** Application Information ********************");
        Application application = Application
                .builder()
                .creationDate(LocalDate.now())
                .client(client)
                .build();
        client.setApplication(application);
        log.info("application {} ", application);

        applicationRepository.save(application);

        List<LoanOfferDTO> loanOfferList = conveyorClient.getLoanOffers(loanApplicationRequest)
                .stream()
                .peek(offer -> offer
                .setApplicationId(application.getId()))
                .collect(Collectors.toList());

        log.info("loanOfferList from conveyor {}", loanOfferList);
        return loanOfferList;
    }

    @Transactional
    public void updateApplication(LoanOfferDTO loanOffer) {
        Application application = applicationRepository
                .findById(loanOffer.getApplicationId())
                .orElseThrow(() ->  new RecordNotFoundException("The record not found"));
        log.info("application {} ", application);
        application.setStatus(PREAPPROVAL);
        application.setStatusHistory(
                List.of(
                ApplicationStatusHistoryDTO
                        .builder()
                        .status(PREAPPROVAL)
                        .time(LocalDate.now())
                        .changeType(AUTOMATIC)
                        .build()
        ));
        log.info("application status {} ", application.getStatus());
        log.info("application status history {} ", application.getStatusHistory());
        application.setAppliedOffer(loanOffer);
        log.info("applied offer {} ", application.getAppliedOffer());
        applicationRepository.save(application);

        EmailMessage emailMessage = EmailMessage.builder()
                .applicationId(application.getId())
                .address(application.getClient().getEmail())
                .theme(FINISH_REGISTRATION)
                .build();
        messageProducer.sendFinishRegistrationMessage(emailMessage);
        log.info("message sent => {}" , emailMessage);
    }

    @Transactional
    public void creditCreation(Long applicationId, ScoringDataDTO scoringData){
        CreditDTO conveyorCredit;
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryList;
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new RecordNotFoundException("The record not found"));
        log.info("application {} ",application);
        applicationStatusHistoryList = application.getStatusHistory();
        try {
            conveyorCredit = conveyorClient.getCredit(scoringData);
            log.info("Credit from conveyor {} ", conveyorCredit);
        }catch (Exception e){
            log.info("Credit conveyor denied application {}",e.getMessage());
            application.setStatus(CC_DENIED);
            applicationStatusHistoryList.add(
                    ApplicationStatusHistoryDTO
                            .builder()
                            .status(CC_DENIED)
                            .time(LocalDate.now())
                            .changeType(MANUAL)
                            .build()
            );
            application.setStatusHistory(applicationStatusHistoryList);
            EmailMessage message = EmailMessage.builder()
                    .applicationId(applicationId)
                    .theme(APPLICATION_DENIED)
                    .address(application.getClient().getEmail())
                    .build();
            messageProducer.sendApplicationDeniedMessage(message);
            return;
        }

        application.setStatus(CC_APPROVED);
        applicationStatusHistoryList.add(
                ApplicationStatusHistoryDTO
                        .builder()
                        .status(CC_APPROVED)
                        .time(LocalDate.now())
                        .changeType(MANUAL)
                        .build());
        application.setStatusHistory(applicationStatusHistoryList);
       documentService.sendingCreateDocuments(applicationId);

        Client client = application.getClient();
        log.info("******************** Client information ********************");
        client.setDependentNumber(scoringData.getDependentAmount());
        client.setAccount(scoringData.getAccount());
        client.setGender(scoringData.getGender());
        client.setMaritalStatus(scoringData.getMaritalStatus());
        client.setEmployment(scoringData.getEmployment());
        log.info("******************** Passport information ********************");
        Passport passport = Passport
                .builder()
                .series(scoringData.getPassportSeries())
                .issue_date(scoringData.getPassportIssueDate())
                .issue_branch(scoringData.getPassportIssueBranch())
                .number(scoringData.getPassportNumber())
                .build();
        log.info("passport {} ", passport);
        client.setPassport(passport);
        log.info("client {} ", client);
        log.info("******************** Credit information ********************");
        Credit credit =  Credit
                .builder()
                .creditStatus(CALCULATED)
                .flc(conveyorCredit.getPsk())
                .amount(conveyorCredit.getAmount())
                .application(application)
                .term(conveyorCredit.getTerm())
                .rate(conveyorCredit.getRate())
                .isSalaryClient(conveyorCredit.getIsSalaryClient())
                .isInsuranceEnabled(conveyorCredit.getIsInsuranceEnabled())
                .paymentSchedule(conveyorCredit.getPaymentSchedule())
                .build();
        application.setCredit(credit);
        log.info("client {} ", client);
        applicationRepository.save(application);
    }

}
