package com.enfint.deal.controller;

import com.enfint.deal.dto.EmploymentDTO;
import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import com.enfint.deal.exception.RecordNotFoundException;
import com.enfint.deal.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.enfint.deal.dataEnum.EmploymentStatus.EMPLOYED;
import static com.enfint.deal.dataEnum.Gender.MALE;
import static com.enfint.deal.dataEnum.MaritalStatus.SINGLE;
import static com.enfint.deal.dataEnum.Position.WORKER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DealController.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;


    @Test
    void shouldGetListOfLoanOffers() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequest = LoanApplicationRequestDTO.builder()
                .email("boitumelotshehla@gmail.com")
                .amount(BigDecimal.valueOf(10000))
                .birthdate(LocalDate.of(1999, 1, 21))
                .firstName("Boitumelo")
                .lastName("Tshehla")
                .middleName("Tumi")
                .term(10)
                .passportNumber("698534")
                .passportSeries("4265")
                .build();
        ObjectMapper mapper = JsonMapper.builder()
                .build().registerModule(new JavaTimeModule());
        String loanRequest = mapper.writeValueAsString(loanApplicationRequest);
        LoanOfferDTO loanOfferDTO1 = LoanOfferDTO.builder().rate(BigDecimal.valueOf(13)).build();
        LoanOfferDTO loanOfferDTO3 = LoanOfferDTO.builder().rate(BigDecimal.valueOf(17)).build();
        LoanOfferDTO loanOfferDTO2 = LoanOfferDTO.builder().rate(BigDecimal.valueOf(20)).build();
        LoanOfferDTO loanOfferDTO4 = LoanOfferDTO.builder().rate(BigDecimal.valueOf(6)).build();
        when(applicationService.getListOfLoanOffers(loanApplicationRequest))
                .thenReturn(List.of(loanOfferDTO1,loanOfferDTO2,loanOfferDTO3,loanOfferDTO4));
        mockMvc.perform(post("http://localhost:8082/deal/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].rate").value(BigDecimal.valueOf(13).toString()))
                .andDo(print());
    }

    @Test
    void shouldUpdateApplicationWithSelectLoanOffer() throws Exception {
        String loanOfferRequest;
        ObjectMapper mapper = JsonMapper.builder().build();
        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(10000))
                .totalAmount(BigDecimal.valueOf(11132.70))
                .term(10)
                .monthlyPayment(BigDecimal.valueOf(1113.27))
                .rate(BigDecimal.valueOf(24))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
        doNothing().when(applicationService).updateApplication(loanOffer);
        loanOfferRequest = mapper.writeValueAsString(loanOffer);
        mockMvc.perform(put("http://localhost:8082/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanOfferRequest))
                .andExpect(status().isOk())
                .andDo(print());
        verify(applicationService).updateApplication(loanOffer);
    }
    @Test
    void shouldThrowRecordNotFoundWhenIdIsInvalid() throws Exception {
        String loanOfferRequest;
        ObjectMapper mapper = JsonMapper.builder().build();
        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .applicationId(2L)
                .requestedAmount(BigDecimal.valueOf(10000))
                .totalAmount(BigDecimal.valueOf(11132.70))
                .term(10)
                .monthlyPayment(BigDecimal.valueOf(1113.27))
                .rate(BigDecimal.valueOf(24))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
        doThrow(RecordNotFoundException.class).when(applicationService).updateApplication(loanOffer);
        loanOfferRequest = mapper.writeValueAsString(loanOffer);
        mockMvc.perform(put("http://localhost:8082/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanOfferRequest))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldCreateCreditAndUpdateApplication() throws Exception {
        String scoringDataRequest;
        Long applicationId = 1L;
        ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(EMPLOYED)
                .employerINN("enfint")
                .salary(BigDecimal.valueOf(90000))
                .position(WORKER)
                .workExperienceTotal(25)
                .workExperienceCurrent(15)
                .build();
        ScoringDataDTO scoringData = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000))
                .term(10)
                .firstName("Boitumelo")
                .lastName("Tshehla")
                .middleName("Tumi")
                .gender(MALE)
                .birthdate(LocalDate.of(1999, 1, 21))
                .passportNumber("698534")
                .passportSeries("4265")
                .passportIssueBranch("johannesburg")
                .maritalStatus(SINGLE)
                .dependentAmount(1)
                .employment(employment)
                .account("FNB")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
        doNothing().when(applicationService).creditCreation(applicationId,scoringData);
        scoringDataRequest = mapper.writeValueAsString(scoringData);
        mockMvc.perform(put("http://localhost:8082/deal/calculate/{applicationId}",applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scoringDataRequest))
                .andExpect(status().isOk())
                .andDo(print());
        verify(applicationService).creditCreation(applicationId,scoringData);

    }
}