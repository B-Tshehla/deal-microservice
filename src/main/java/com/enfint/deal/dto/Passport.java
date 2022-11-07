package com.enfint.deal.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class Passport {
    private String series;
    private String number;
    private LocalDate issue_date;
    private String issue_branch;
}
