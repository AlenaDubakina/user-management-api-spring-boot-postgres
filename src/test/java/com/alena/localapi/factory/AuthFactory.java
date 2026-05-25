package com.alena.localapi.factory;

import com.alena.localapi.auth.dto.RegisterRequestDTO;

import static com.alena.localapi.utils.TestUtils.generateRandomEmail;

public final class AuthFactory {
    private AuthFactory() {
    }

    public static RegisterRequestDTO validRegisterRequest() {
        return new RegisterRequestDTO(generateRandomEmail(), "testPassword1245");
    }
}