package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class InMemPaymentRepositoryTest {

    private PaymentRepository underTest;
    private static final Payment PAYMENT1 = new Payment(1, 100.5, "Payment No. 1");
    private static final Payment PAYMENT2 = new Payment(2, 350.65, "Payment No.2");;
    private static final Payment PAYMENT3 = new Payment(3, 500.30, "Payment No. 3");

    @BeforeEach
    public void setUp() {
        underTest = new InMemPaymentRepository();
    }

    @Test
    @DisplayName("When trying to save a unique payment in the repository, it should get saved and return a copy")
    public void shouldSavePayment_andReturnACopy() {
        Payment payment = underTest.save(PAYMENT1);

        assertAll(
                () -> assertThat(payment).isNotNull(),
                () -> assertThat(payment).isEqualTo(PAYMENT1),
                () -> assertThat(payment.getMessage()).isEqualTo(PAYMENT1.getMessage())
        );
    }

    @Test
    @DisplayName("When trying to save a null payment, it should throw IllegalArgumentException")
    public void shouldThrowException_whenPaymentIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.save(null));
    }

    @Test
    @DisplayName("When trying to save an existing payment, it should throw IllegalArgumentException")
    public void shouldThrowException_whenPaymentExists() {
        Payment payment = underTest.save(PAYMENT1);

        assertAll(
                () -> assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> underTest.save(PAYMENT1)),
                () -> assertThat(payment).isNotNull(),
                () -> assertThat(payment).isEqualTo(PAYMENT1),
                () -> assertThat(payment.getMessage()).isEqualTo(PAYMENT1.getMessage())
        );
    }

    @Test
    @DisplayName("When trying to change the message of an existing payment, only it should get changed")
    public void shouldChangeMessage_whenPaymentExists() {
        underTest.save(PAYMENT1);
        Payment payment = underTest.editMessage(PAYMENT1.getPaymentId(), "This is a changed payment message");

        assertAll(
                () -> assertThat(payment).isNotNull(),
                () -> assertThat(payment.getMessage()).isEqualTo("This is a changed payment message"),
                () -> assertThat(payment).isEqualTo(PAYMENT1)
        );
    }

    @Test
    @DisplayName("When trying to change the message of a non-existing payment, should throw NoSuchElementException")
    public void shouldThrowException_whenPaymentUUIDIsNull() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> underTest.editMessage(null, "It doesn't matter, it won't get changed"));
    }

    @Test
    @DisplayName("When trying to get a payment that exists by UUID, it should return a copy of this payment")
    public void shouldReturnPayment_whenIdExists() {
        underTest.save(PAYMENT1);
        Optional<Payment> payment = underTest.findById(PAYMENT1.getPaymentId());

        assertAll(
                () -> assertThat(payment.isPresent()).isTrue(),
                () -> assertThat(payment.get()).isEqualTo(PAYMENT1),
                () -> assertThat(payment.get().getMessage()).isEqualTo(PAYMENT1.getMessage())
        );
    }

    @Test
    @DisplayName("When trying to get a payment by a null UUID, it should throw IllegalArgumentException")
    public void shouldThrowException_whenIdIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.findById(null));
    }

    @Test
    @DisplayName("When trying to get all the payments from the repo, it should return a list with all payments")
    public void shouldReturnAListWithAllPayments() {
        underTest.save(PAYMENT1);
        underTest.save(PAYMENT2);
        underTest.save(PAYMENT3);
        List<Payment> actualPayments = underTest.findAll();
        List<Payment> expectedPayments = Arrays.asList(PAYMENT1, PAYMENT2, PAYMENT3);

        assertAll(
                () -> assertThat(actualPayments).isNotNull(),
                () -> assertThat(actualPayments.size()).isEqualTo(expectedPayments.size()),
                () -> assertThat(actualPayments.containsAll(expectedPayments)).isTrue(),
                () -> assertThat(expectedPayments.containsAll(actualPayments)).isTrue()
        );
    }
}