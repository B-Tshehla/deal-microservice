package com.enfint.deal.kafka;

import com.enfint.deal.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {


    private final NewTopic createDocuments;
    private final NewTopic finishRegistration;
    private final NewTopic sendDocuments;
    private final NewTopic sendSes;
    private final NewTopic creditIssued;
    private final NewTopic applicationDenied;

    private final KafkaTemplate<String, EmailMessage>  kafkaTemplate;

    public void sendFinishRegistrationMessage(EmailMessage emailMessage){
        log.info("message event => {}",emailMessage.toString());

        Message<EmailMessage> message = MessageBuilder
                .withPayload(emailMessage)
                .setHeader(TOPIC,finishRegistration.name())
                .build();
        kafkaTemplate.send(message);
    }

    public void sendCreatingDocumentsMessage(EmailMessage emailMessage){
        log.info("message event => {}",emailMessage.toString());
        Message<EmailMessage> message = MessageBuilder
                .withPayload(emailMessage)
                .setHeader(TOPIC,createDocuments.name())
                .build();
        kafkaTemplate.send(message);
    }

    public void sendDocumentsMessage(EmailMessage emailMessage){
        log.info("message event => {}",emailMessage.toString());
        Message<EmailMessage> message = MessageBuilder
                .withPayload(emailMessage)
                .setHeader(TOPIC,sendDocuments.name())
                .build();
        kafkaTemplate.send(message);
    }
    public void sendSesCodeMessage(EmailMessage emailMessage){
        log.info("message event => {}",emailMessage.toString());
        Message<EmailMessage> message = MessageBuilder
                .withPayload(emailMessage)
                .setHeader(TOPIC,sendSes.name())
                .build();
        kafkaTemplate.send(message);
    }
    public void sendCreditIssuedMessage(EmailMessage emailMessage){
        log.info("message event => {}",emailMessage.toString());
        Message<EmailMessage> message = MessageBuilder
                .withPayload(emailMessage)
                .setHeader(TOPIC,creditIssued.name())
                .build();
        kafkaTemplate.send(message);
    }

    public void sendApplicationDeniedMessage(EmailMessage emailMessage) {
        log.info("message event => {}", emailMessage.toString());
        Message<EmailMessage> message = MessageBuilder
                .withPayload(emailMessage)
                .setHeader(TOPIC, applicationDenied.name())
                .build();
        kafkaTemplate.send(message);
    }
}
