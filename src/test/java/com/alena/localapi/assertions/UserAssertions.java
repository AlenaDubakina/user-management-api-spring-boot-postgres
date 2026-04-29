package com.alena.localapi.assertions;

import com.alena.localapi.dto.UserResponseDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAssertions {
    public static void assertUserIdIsPositive(UserResponseDTO userResponseDTO) {
        assertThat(userResponseDTO.getId())
                .as("У пользователя невалидный id %d", userResponseDTO.getId())
                .isPositive();
    }

    public static void assertUserHasValidEmail(UserResponseDTO userResponseDTO) {
        assertThat(userResponseDTO.getEmail())
                .as("У пользователя невалидный email %s".formatted(userResponseDTO.getEmail()))
                .contains("@");
    }

    public static void assertEmailFieldIsNotEmpty(UserResponseDTO userResponseDTO) {
        assertThat(userResponseDTO.getEmail())
                .as("Поле email не должно быть пустым")
                .isNotEmpty();
    }

    public static void assertUserEquals(UserResponseDTO actualUser, UserResponseDTO expectedUser) {
        assertThat(actualUser)
                .usingRecursiveComparison()
                .as("Пользователь не соответствует ожидаемому")
                .isEqualTo(expectedUser);
    }

    public static void assertUsersIsNotEmpty(List<UserResponseDTO> users) {
        assertThat(users)
                .as("Список пользователей пустой")
                .isNotEmpty();
    }

    public static void assertUserEmailIsEquals(UserResponseDTO userResponseDTO, String email) {
        assertThat(userResponseDTO.getEmail())
                .as("У пользователя должен быть email %s, но был %s".formatted(email, userResponseDTO.getEmail()))
                .isEqualTo(email);
    }

    public static void assertUserHasRequiredFields(UserResponseDTO userResponseDTO) {
        assertUserIdIsPositive(userResponseDTO);
        assertEmailFieldIsNotEmpty(userResponseDTO);
        assertUserHasValidEmail(userResponseDTO);
    }
}