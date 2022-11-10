package com.enfint.deal.dto;

import com.enfint.deal.dataEnum.ChangeType;
import com.enfint.deal.dataEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
public class ApplicationStatusHistoryDTO {
    private Status status;
    private LocalDate time;
    private ChangeType changeType;
}

