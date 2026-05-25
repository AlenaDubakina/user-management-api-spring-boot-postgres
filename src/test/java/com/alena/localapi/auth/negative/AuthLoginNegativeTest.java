package com.alena.localapi.auth.negative;

import com.alena.localapi.auth.dto.LoginRequestDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrorResponse;
import static com.alena.localapi.factory.AuthFactory.validRegisterRequest;

public class AuthLoginNegativeTest extends BaseTest {
    @Test
    public void login_should_fail_with_wrong_password() {
        RegisterRequestDTO registerRequestDTO = validRegisterRequest();

        registerUser(registerRequestDTO);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(registerRequestDTO.getEmail(), "newPassword12345");

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.AUTH_LOGIN, loginRequestDTO)
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO,
                401,
                "Неверные учетные данные",
                "Unauthorized",
                ApiEndpoints.AUTH_LOGIN);
    }

    @Test
    public void login_should_fail_with_non_existing_user() {
        RegisterRequestDTO registerRequestDTO = validRegisterRequest();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(registerRequestDTO.getEmail(), registerRequestDTO.getPassword());

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.AUTH_LOGIN, loginRequestDTO)
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO,
                401,
                "Неверные учетные данные",
                "Unauthorized",
                ApiEndpoints.AUTH_LOGIN);
    }
}