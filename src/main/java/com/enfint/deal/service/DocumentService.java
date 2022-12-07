package com.enfint.deal.service;



import com.enfint.deal.dataEnum.Status;
import com.enfint.deal.dataEnum.Theme;
import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.EmailMessage;
import com.enfint.deal.exception.applicationDenied.ApplicationDeniedException;
import com.enfint.deal.exception.recordNotFound.RecordNotFoundException;
import com.enfint.deal.kafka.MessageProducer;
import com.enfint.deal.model.Application;
import com.enfint.deal.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



import static com.enfint.deal.dataEnum.ChangeType.MANUAL;
import static com.enfint.deal.dataEnum.Theme.CREDIT_ISSUED;


@Service
@AllArgsConstructor
@Slf4j
public class DocumentService {
    private ApplicationRepository applicationRepository;
    private MessageProducer messageProducer;

    public void sendingCreateDocuments(Long applicationId){
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->  new RecordNotFoundException("The record not found"));

        messageProducer.sendCreatingDocumentsMessage(EmailMessage.builder()
                .applicationId(applicationId)
                .theme(Theme.CREATE_DOCUMENTS)
                .address(application.getClient().getEmail())
                .build());
    }
    public void sendingDocuments(Long applicationId){
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryList;
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(()-> new RecordNotFoundException("Record not found"));

        application.setStatus(Status.PREPARE_DOCUMENTS);
        applicationStatusHistoryList = application.getStatusHistory();
        applicationStatusHistoryList.add(ApplicationStatusHistoryDTO
                .builder()
                .status(Status.CC_APPROVED)
                .time(LocalDate.now())
                .changeType(MANUAL)
                .build());
        application.setStatusHistory(applicationStatusHistoryList);
        applicationRepository.save(application);

        messageProducer.sendDocumentsMessage(EmailMessage
                .builder()
                .applicationId(applicationId)
                .theme(Theme.SEND_DOCUMENTS)
                .address(application.getClient().getEmail())
                .build());
    }

    public void signingDocuments(Long applicationId){
        int min = 1_000;
        int max = 10_000;
        int sesCode;

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(()-> new RecordNotFoundException("Record not found"));

        sesCode = (int) (Math.random()*(max - min)+ min);
        application.setSesCode(sesCode);
        applicationRepository.save(application);

        messageProducer.sendDocumentsMessage(EmailMessage
                .builder()
                .applicationId(applicationId)
                .theme(Theme.SEND_SES)
                .address(application.getClient().getEmail())
                .build());
    }

    public void verifySesCode(Long applicationId, Integer sesCode){
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryList;
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(()-> new RecordNotFoundException("Record not found"));
        if(sesCode != application.getSesCode()){
            log.info("SES code is incorrect");
            throw new ApplicationDeniedException("SES code is incorrect");
        }
        application.setStatus(Status.DOCUMENT_SIGNED);
        applicationStatusHistoryList = application.getStatusHistory();
        applicationStatusHistoryList.add(ApplicationStatusHistoryDTO
                .builder()
                .status(Status.DOCUMENT_SIGNED)
                .time(LocalDate.now())
                .changeType(MANUAL)
                .build());
        application.setStatusHistory(applicationStatusHistoryList);

        application.setStatus(Status.CREDIT_ISSUED);
        applicationStatusHistoryList.add(ApplicationStatusHistoryDTO
                .builder()
                .status(Status.CREDIT_ISSUED)
                .time(LocalDate.now())
                .changeType(MANUAL)
                .build());
        application.setStatusHistory(applicationStatusHistoryList);
        applicationRepository.save(application);
        messageProducer.sendDocumentsMessage(EmailMessage
                .builder()
                .applicationId(applicationId)
                .theme(CREDIT_ISSUED)
                .address(application.getClient().getEmail())
                .build());
    }

}
