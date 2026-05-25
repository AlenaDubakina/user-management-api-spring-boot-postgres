package com.alena.localapi.auth.positive;

import com.alena.localapi.auth.dto.LoginRequestDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.factory.AuthFactory.validRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthLoginPositiveTest extends BaseTest {
    @Test
    public void login_user_should_return_token() {
        RegisterRequestDTO registerRequestDTO = validRegisterRequest();

        registerUser(registerRequestDTO);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(registerRequestDTO.getEmail(), registerRequestDTO.getPassword());

        String token = testClient.post(ApiEndpoints.AUTH_LOGIN, loginRequestDTO)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        assertThat(token)
                .as("Токен невалидный")
                .isNotBlank();
    }
}