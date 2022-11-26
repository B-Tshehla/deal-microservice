package com.enfint.deal.service;


import com.enfint.deal.dto.LoanOfferDTO;
import com.enfint.deal.dto.ScoringDataDTO;
import com.enfint.deal.dto.EmploymentDTO;
import com.enfint.deal.dto.CreditDTO;
import com.enfint.deal.dto.LoanApplicationRequestDTO;
import com.enfint.deal.exception.recordNotFound.RecordNotFoundException;
import com.enfint.deal.fiegnClient.ConveyorClient;
import com.enfint.deal.kafka.MessageProducer;
import com.enfint.deal.model.Application;
import com.enfint.deal.model.Client;
import com.enfint.deal.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.enfint.deal.dataEnum.EmploymentStatus.EMPLOYED;
import static com.enfint.deal.dataEnum.Gender.MALE;
import static com.enfint.deal.dataEnum.MaritalStatus.SINGLE;
import static com.enfint.deal.dataEnum.Position.WORKER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ConveyorClient conveyorClient;

    @Mock
    private MessageProducer messageProducer;
    @InjectMocks
    private ApplicationService underTest;
    private LoanOfferDTO loanOffer;
    private ScoringDataDTO scoringData;

    @BeforeEach
    void setUp() {
        underTest = new ApplicationService(applicationRepository, conveyorClient,messageProducer);

    }

    @Test
    void shouldGetListOfLoanOffersAndVerifyDataCapture() {
        //Given
        List<LoanOfferDTO> listOffers = List.of(
                LoanOfferDTO.builder()
                        .applicationId(1L)
                        .requestedAmount(BigDecimal.valueOf(10000))
                        .totalAmount(BigDecimal.valueOf(11132.70))
                        .term(10)
                        .monthlyPayment(BigDecimal.valueOf(1113.27))
                        .rate(BigDecimal.valueOf(24))
                        .isInsuranceEnabled(false)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDTO.builder()
                        .applicationId(1L)
                        .requestedAmount(BigDecimal.valueOf(10000))
                        .totalAmount(BigDecimal.valueOf(10747.90))
                        .term(10)
                        .monthlyPayment(BigDecimal.valueOf(1074.79))
                        .rate(BigDecimal.valueOf(16))
                        .isInsuranceEnabled(false)
                        .isSalaryClient(true)
                        .build(),
                LoanOfferDTO.builder()
                        .applicationId(1L)
                        .requestedAmount(BigDecimal.valueOf(10000))
                        .totalAmount(BigDecimal.valueOf(10652.90))
                        .term(10)
                        .monthlyPayment(BigDecimal.valueOf(1065.29))
                        .rate(BigDecimal.valueOf(14))
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDTO.builder()
                        .applicationId(1L)
                        .requestedAmount(BigDecimal.valueOf(10000))
                        .totalAmount(BigDecimal.valueOf(10277.10))
                        .term(10)
                        .monthlyPayment(BigDecimal.valueOf(1027.71))
                        .rate(BigDecimal.valueOf(6))
                        .isInsuranceEnabled(true)
                        .isSalaryClient(true)
                        .build()
        );
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
        //When
        when(conveyorClient.getLoanOffers(loanApplicationRequest)).thenReturn(listOffers);
        underTest.getListOfLoanOffers(loanApplicationRequest);
        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);

        //Then
        verify(applicationRepository).save(applicationArgumentCaptor.capture());
        assertThat(underTest.getListOfLoanOffers(loanApplicationRequest).size()).isEqualTo(4);
    }

    @Test
    void shouldVerifyThatJPAFunctionsWhereInvoked() {
        //Given
        Long id = 1L;
        Application application = Application
                .builder()
                .id(1L)
                .creationDate(LocalDate.now())
                .client(new Client())
                .build();
        loanOffer = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(10000))
                .totalAmount(BigDecimal.valueOf(10747.90))
                .term(10)
                .monthlyPayment(BigDecimal.valueOf(1074.79))
                .rate(BigDecimal.valueOf(16))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();
        //When
        when(applicationRepository.findById(id)).thenReturn(Optional.ofNullable(application));
        underTest.updateApplication(loanOffer);
        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        //Then
        verify(applicationRepository).findById(id);
        verify(applicationRepository).save(applicationArgumentCaptor.capture());
    }
    @Test
    void shouldThrowARecordNotFoundExceptionWhenIdDoesNotExist() {
        Long id = 1L;
        Application application = Application
                .builder()
                .id(1L)
                .creationDate(LocalDate.now())
                .client(new Client())
                .build();
        loanOffer = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(10000))
                .totalAmount(BigDecimal.valueOf(10747.90))
                .term(10)
                .monthlyPayment(BigDecimal.valueOf(1074.79))
                .rate(BigDecimal.valueOf(16))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();
        assertThatThrownBy(()-> underTest.updateApplication(loanOffer))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The record not found");
        verify(applicationRepository).findById(id);
        verify(applicationRepository,never()).save(application);
    }

    @Test
    void ShouldVerifyThatJPARepositoriesWhereInvoked(){
        //Given
        Long id = 1L;
        Application application = Application
                .builder()
                .id(1L)
                .creationDate(LocalDate.now())
                .client(new Client())
                .build();
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(EMPLOYED)
                .employerINN("enfint")
                .salary(BigDecimal.valueOf(90000))
                .position(WORKER)
                .workExperienceTotal(25)
                .workExperienceCurrent(15)
                .build();
        scoringData = ScoringDataDTO
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
        //When
        when(applicationRepository.findById(id)).thenReturn(Optional.ofNullable(application));
        when(conveyorClient.getCredit(scoringData)).thenReturn(new CreditDTO());
        underTest.creditCreation(id,scoringData);
        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        //Then
        verify(applicationRepository).findById(id);
        verify(applicationRepository).save(applicationArgumentCaptor.capture());
    }
    @Test
    void shouldThrowARecordNotFoundExceptionWhenIdIsInvalid() {
        Long id = 1L;
        Application application = Application
                .builder()
                .id(1L)
                .creationDate(LocalDate.now())
                .client(new Client())
                .build();
        assertThatThrownBy(()-> underTest.creditCreation(id,scoringData))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The record not found");
        verify(applicationRepository).findById(id);
        verify(applicationRepository,never()).save(application);
    }
}