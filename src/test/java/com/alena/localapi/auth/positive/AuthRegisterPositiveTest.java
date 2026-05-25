package com.alena.localapi.auth.positive;

import com.alena.localapi.auth.dto.AuthResponseDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.base.BaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRegisterPositiveTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.UserDataProvider#validUserData")
    public void register_user_should_return_token(String email, String password) {
        RegisterRequestDTO registerUserDto = new RegisterRequestDTO(email, password);

        AuthResponseDTO authResponseDTO = registerUser(registerUserDto);

        assertThat(authResponseDTO.getToken())
                .as("Токен невалидный")
                .isNotBlank();
    }
}