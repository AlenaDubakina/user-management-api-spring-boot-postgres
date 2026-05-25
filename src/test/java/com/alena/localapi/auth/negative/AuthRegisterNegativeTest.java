package com.alena.localapi.auth.negative;

import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrorResponse;
import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrors;
import static com.alena.localapi.factory.AuthFactory.validRegisterRequest;

public class AuthRegisterNegativeTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.UserDataProvider#invalidUserData")
    public void register_InvalidEmailFormat_ShouldReturn400BadRequest(String email, String password, List<String> expectedFields) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(email, password);

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.AUTH_REGISTER, registerRequestDTO)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrors(errorResponseDTO,
                400,
                "Validation failed",
                "Bad Request",
                ApiEndpoints.AUTH_REGISTER,
                expectedFields);
    }

    @Test
    public void register_alreadyExistsEmail_negative() {
        RegisterRequestDTO userAlreadyExists = validRegisterRequest();

        registerUser(userAlreadyExists);

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.AUTH_REGISTER, userAlreadyExists)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 409, "Пользователь с таким email %s уже существует"
                .formatted(userAlreadyExists.getEmail()), "Conflict", ApiEndpoints.AUTH_REGISTER);
    }
}