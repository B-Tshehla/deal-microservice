package com.enfint.deal.dto;

import com.enfint.deal.dataEnum.Status;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ApplicationStatusHistoryDTO {
    private Status status;
    private LocalDateTime time;
    private Status changeType;
}

