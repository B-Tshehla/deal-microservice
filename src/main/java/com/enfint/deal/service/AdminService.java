package com.enfint.deal.service;

import com.enfint.deal.dataEnum.Status;
import com.enfint.deal.dto.ApplicationModelDTO;
import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.ClientModelDTO;
import com.enfint.deal.dto.CreditModelDTO;
import com.enfint.deal.exception.recordNotFound.RecordNotFoundException;
import com.enfint.deal.model.Application;
import com.enfint.deal.model.Client;
import com.enfint.deal.model.Credit;
import com.enfint.deal.repository.ApplicationRepository;
import com.enfint.deal.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.enfint.deal.dataEnum.ChangeType.MANUAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    public void updateApplicationStatus(Long applicationId){
        log.info("updating status ");
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryList;
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->  new RecordNotFoundException("The record not found"));
        log.info("application {} ", application);
        applicationStatusHistoryList = application.getStatusHistory();
        application.setStatus(Status.DOCUMENT_CREATED);
        applicationStatusHistoryList.add(ApplicationStatusHistoryDTO
                .builder()
                .status(Status.DOCUMENT_CREATED)
                .time(LocalDate.now())
                .changeType(MANUAL)
                .build());
        application.setStatusHistory(applicationStatusHistoryList);
        applicationRepository.save(application);
    }

    public ApplicationModelDTO getApplicationById(Long applicationId){
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->  new RecordNotFoundException("The record not found"));
        log.info("application {} ", application.toString());
        Client client = application.getClient();

        ClientModelDTO clientModelDTO = ClientModelDTO.builder()
                .email(client.getEmail())
                .account(client.getAccount())
                .birthDate(client.getBirthDate())
                .dependentNumber(client.getDependentNumber())
                .employment(client.getEmployment())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(client.getGender())
                .passport(client.getPassport())
                .maritalStatus(client.getMaritalStatus())
                .id(client.getId())
                .build();
        return ApplicationModelDTO
                .builder()
                .id(application.getId())
                .creationDate(application.getCreationDate())
                .appliedOffer(application.getAppliedOffer())
                .signDate(application.getSignDate())
                .statusHistory(application.getStatusHistory())
                .client(clientModelDTO)
                .sesCode(application.getSesCode())
                .status(application.getStatus())
                .build();
    }

    public ClientModelDTO getClientByFistNameAndLastName(String firstName, String lastName){
        List<Client> getAllClients = clientRepository.findAll();
        Client client = getAllClients.stream()
                .filter(c -> c.getFirstName().equals(firstName))
                .filter(c -> c.getLastName().equals(lastName))
                .findFirst().orElseThrow(() ->  new RecordNotFoundException("The record not found"));
        return ClientModelDTO.builder()
                .lastName(client.getLastName())
                .firstName(client.getFirstName())
                .middleName(client.getMiddleName())
                .id(client.getId())
                .account(client.getAccount())
                .maritalStatus(client.getMaritalStatus())
                .email(client.getEmail())
                .passport(client.getPassport())
                .gender(client.getGender())
                .employment(client.getEmployment())
                .dependentNumber(client.getDependentNumber())
                .birthDate(client.getBirthDate())
                .build();
    }
}

