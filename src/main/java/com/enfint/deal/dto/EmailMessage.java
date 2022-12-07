package com.enfint.deal.dto;

import com.enfint.deal.dataEnum.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailMessage {
    String address;
    Theme theme;
    Long applicationId;
}
