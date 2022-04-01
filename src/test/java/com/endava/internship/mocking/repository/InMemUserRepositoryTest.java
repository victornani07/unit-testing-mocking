package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.jws.soap.SOAPBinding;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class InMemUserRepositoryTest {

    private UserRepository underTest;

    @BeforeEach
    public void setUp() {
        underTest = new InMemUserRepository();
    }

    @Test
    @DisplayName("When trying to get a user by null ID, it should throw IllegalArgumentException")
    public void shouldThrowException_whenIdIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.findById(null));
    }

    @Test
    @DisplayName("When trying to get a user by an ID that exists, it should get the user with that ID")
    public void shouldReturnUser_whenIdExists() {
        Optional<User> user = underTest.findById(3);

        assertAll(
                () -> assertThat(user.isPresent()).isTrue(),
                () -> assertThat(user.get().getId()).isEqualTo(3),
                () -> assertThat(user.get().getName()).isEqualTo("Peter"),
                () -> assertThat(user.get().getStatus()).isEqualTo(Status.INACTIVE)
        );
    }
}