package com.alena.localapi.assertions;

import com.alena.localapi.dto.ErrorResponseDTO;
import org.assertj.core.api.SoftAssertions;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorAssertions {

    public static void assertValidationErrorResponse(
            ErrorResponseDTO errorResponseDTO,
            Integer expectedStatus,
            String expectedMessage,
            String expectedError,
            String expectedPath) {

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(errorResponseDTO.getStatus())
                .as("Некорректный статус ошибки")
                .isEqualTo(expectedStatus);

        softly.assertThat(errorResponseDTO.getMessage())
                .as("Некорректное сообщение ошибки")
                .isEqualTo(expectedMessage);

        softly.assertThat(errorResponseDTO.getError())
                .as("Некорректный текст HTTP статуса")
                .contains(expectedError);

        softly.assertThat(errorResponseDTO.getPath())
                .as("Некорректный path")
                .contains(expectedPath);

        softly.assertThat(errorResponseDTO.getTimestamp())
                .as("Timestamp не должен быть null")
                .isNotNull();

        softly.assertAll();
    }

    public static void assertFieldError(Map<String, String> errors, String field) {
        assertThat(errors)
                .as("Ожидалась ошибка для поля %s, но пришли %s".formatted(field, errors))
                .containsKey(field);
    }

    public static void assertErrorsSize(Map<String, String> errors, int expectedSize) {
        assertThat(errors)
                .as("Список ошибок содержит некорректное количество элементов")
                .hasSize(expectedSize);
    }
}