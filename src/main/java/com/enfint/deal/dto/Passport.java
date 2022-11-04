package com.enfint.deal.dto;

import lombok.Data;

import java.util.Date;
@Data
public class Passport {
    private String series;
    private String number;
    private Date issue_date;
    private String issue_branch;
}
