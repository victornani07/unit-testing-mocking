package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.InMemPaymentRepository;
import com.endava.internship.mocking.repository.InMemUserRepository;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService underTest;
    private PaymentRepository paymentRepository;
    private ValidationService validationService;
    private UserRepository userRepository;
    private static final Double AMOUNT = 44.44;
    private static final Integer USER_ID = 1;
    private static final UUID UUID1 = UUID.randomUUID();
    private static final String MESSAGE = "This is a new message";
    private static final User USER = new User(USER_ID, "Nani Victor", Status.ACTIVE);

    @BeforeEach
    void setUp() {
        userRepository = mock(InMemUserRepository.class);
        paymentRepository = mock(InMemPaymentRepository.class);
        validationService = mock(BasicValidationService.class);
        underTest = new PaymentService(userRepository, paymentRepository, validationService);
    }

    @Test
    @DisplayName("When creating a payment with valid data, it should register this payment")
    void shouldCreatePayment_whenProperArgumentsAreGiven() {
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

        underTest.createPayment(USER_ID, AMOUNT);

        verify(validationService).validateUserId(USER_ID);
        verify(validationService).validateAmount(AMOUNT);
        verify(validationService, times(1)).validateUser(USER);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        assertAll(
                () -> assertThat(paymentCaptor.getValue().getAmount()).isEqualTo(AMOUNT),
                () -> assertThat(paymentCaptor.getValue().getUserId()).isEqualTo(USER_ID)
        );
    }

    @Test
    @DisplayName("When editing a message of an existing payment, it should modify the message of that exsiting payment")
    void shouldEditMessage_whenProperArgumentsAreGiven() {
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        underTest.editPaymentMessage(UUID1, MESSAGE);

        verify(validationService, times(1)).validateMessage(MESSAGE);
        verify(validationService, times(1)).validatePaymentId(UUID1);
        verify(paymentRepository, times(1))
                .editMessage(uuidCaptor.capture(), messageCaptor.capture());

        assertAll(
                () -> assertThat(uuidCaptor.getValue()).isEqualTo(UUID1),
                () -> assertThat(messageCaptor.getValue()).isEqualTo(MESSAGE)
        );
    }

    @Test
    @DisplayName("When giving a fixed amount, it should return all the payments that exceeds that particular amount")
    void shouldReturnAllExceedingAmounts() {
        Payment payment1 = new Payment(1, 22.33, "Victor Nani");
        Payment payment2 = new Payment(2, 66.66, "Madalina Nani");
        Payment payment3 = new Payment(3, 88.88, "Aurel Vlaicu");
        List<Payment> payments = Arrays.asList(payment1, payment2, payment3);
        List<Double> expectedAmounts = Arrays.asList(66.66, 88.88);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> actualAmounts = underTest.getAllByAmountExceeding(55.55);

        assertAll(
                () -> assertThat(actualAmounts.size()).isEqualTo(2),
                () -> assertThat(actualAmounts.stream().map(Payment::getAmount).collect(Collectors.toList())
                        .containsAll(expectedAmounts)).isTrue()
        );
    }
}
