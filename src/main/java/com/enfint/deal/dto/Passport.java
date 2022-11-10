package com.enfint.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Passport {
    private String series;
    private String number;
    private LocalDate issue_date;
    private String issue_branch;
}
