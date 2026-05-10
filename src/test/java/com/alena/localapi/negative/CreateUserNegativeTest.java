package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import com.alena.localapi.dto.UserRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.alena.localapi.assertions.ErrorAssertions.*;
import static com.alena.localapi.factory.UserFactory.customUser;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class CreateUserNegativeTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.UserDataProvider#invalidUserData")
    public void createUser_shouldReturn400_whenDataInvalid(String email, String password, List<String> expectedFields) {
        UserRequestDTO invalidUser = customUser(email, password);

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.USERS, invalidUser)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 400, "Validation failed", "Bad Request", ApiEndpoints.USERS);
        assertErrorsSize(errorResponseDTO.getErrors(), expectedFields.size());

        expectedFields.forEach(expectedFieldError ->
                assertFieldError(errorResponseDTO.getErrors(), expectedFieldError));
    }

    @Test
    public void createUser_alreadyExists_negative() {
        UserRequestDTO userAlreadyExists = defaultUser();

        createUser(userAlreadyExists);

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.USERS, userAlreadyExists)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 409, "Пользователь с таким email %s уже существует"
                .formatted(userAlreadyExists.getEmail()), "Conflict", ApiEndpoints.USERS);
    }
}