package com.enfint.deal.model;

import com.enfint.deal.dataEnum.Status;
import com.enfint.deal.dto.ApplicationStatusHistoryDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;


@Entity(name = "application")
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Application implements Serializable {
    @Id
    @SequenceGenerator(name = "application_sequence", sequenceName = "application_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_sequence")
    private Long id;
    @Enumerated(STRING)
    private Status status;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    @Type(type = "json")
    private LoanOfferDTO appliedOffer;
    @Column(name = "sign_date")
    private LocalDate signDate;
    @Column(name = "ses_code")
    private String sesCode;
    @Column(name = "status_history", columnDefinition = "jsonb")
    @Type(type = "json")
    private List<ApplicationStatusHistoryDTO> statusHistory;
    @OneToOne(cascade = ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    @OneToOne(cascade = ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private Credit credit;
}
