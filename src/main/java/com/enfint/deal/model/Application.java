package com.enfint.deal.model;

import com.enfint.deal.dataEnum.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;


import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity(name = "application")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
@Data
public class Application {
    @Id
    @SequenceGenerator(
            name = "application_sequence",
            sequenceName = "application_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_sequence"
    )
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Column(
            name = "creation_date"
    )
    private Date creationDate;
    @Column(
            name = "applied_offer"
    )
    private String appliedOffer;
    @Column(
            name = "sign_date"
    )
    private Date signDate;
    @Column(
            name = "ses_code"
    )
    private String sesCode;
    @Column(name = "status_history")
    @Type(type = "jsonb")
    private List<Status> statusHistory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private Credit credit;
}
