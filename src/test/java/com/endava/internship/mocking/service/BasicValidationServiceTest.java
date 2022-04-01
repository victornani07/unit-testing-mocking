package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class BasicValidationServiceTest {

    private ValidationService underTest;
    private static final UUID UUID1 = UUID.randomUUID();
    private static final User USER = new User(1, "Victor Nani", Status.ACTIVE);

    @BeforeEach
    public void setUp() {
        underTest = new BasicValidationService();
    }

    @Test
    @DisplayName("When giving a null amount, it should throw IllegalArgumentException")
    public void shouldThrowException_whenAmountIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateAmount(null));
    }

    @Test
    @DisplayName("When giving an amount that is less or equal to 0, it should throw IllegalArgumentException")
    public void shouldThrowException_whenAmountIsNotPositive() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateAmount(-13.13));
    }

    @Test
    @DisplayName("When giving a correct amount, the method should get called and produce no side effects")
    public void shouldGetCalled_whenAmountIsPositive() {
        assertDoesNotThrow(() -> underTest.validateAmount(15.15));
    }

    @Test
    @DisplayName("When giving a null payment UUID, it should throw IllegalArgumentException")
    public void shouldThrowException_whenUUIDIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validatePaymentId(null));
    }

    @Test
    @DisplayName("When giving a correct payment UUID, the method should get called and produce no side effects")
    public void shouldGetCalled_whenUUIDIsCorrect() {
      assertDoesNotThrow(() -> underTest.validatePaymentId(UUID1));
    }

    @Test
    @DisplayName("When giving a null user ID, it should throw IllegalArgumentException")
    public void shouldThrowException_whenUserIdIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateUserId(null));
    }

    @Test
    @DisplayName("When giving a correct user ID, the method should get called and produce no side effects")
    public void shouldGetCalled_whenUserIdIsCorrect() {
        assertDoesNotThrow(() -> underTest.validateUserId(123));
    }

    @Test
    @DisplayName("When giving an inactive user, it should throw IllegalArgumentException")
    public void shouldThrowException_whenUserIsInactive() {
        User user = new User(1, "Victor Nani", Status.INACTIVE);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateUser(user));
    }

    @Test
    @DisplayName("When giving an active user, the method should get called and produce no side effects")
    public void shouldGetCalled_whenUserIsActive() {
        assertDoesNotThrow(() -> underTest.validateUser(USER));
    }

    @Test
    @DisplayName("When giving a null message, it should throw IllegalArgumentException")
    public void shouldThrowException_whenMessageIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateMessage(null));
    }

    @Test
    @DisplayName("When giving a proper message, the method should get called and produce no side effects")
    public void shouldGetCalled_whenProperMessage() {
        assertDoesNotThrow(() -> underTest.validateMessage("Victor"));
    }
}