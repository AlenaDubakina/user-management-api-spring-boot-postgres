package com.alena.localapi.factory;

import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;

import java.util.UUID;

public final class UserFactory {

    private UserFactory() {
    }

    public static UserRequestDTO defaultUser() {
        return new UserRequestDTO(generateRandomEmail(), "testPassword12345");
    }

    public static UserRequestDTO customUser(String email, String password) {
        return new UserRequestDTO(email, password);
    }

    public static UserPatchDTO patchWithEmail(String email) {
        UserPatchDTO dto = new UserPatchDTO();
        dto.setEmail(email);
        return dto;
    }

    public static UserPatchDTO patchWithPassword(String password) {
        UserPatchDTO dto = new UserPatchDTO();
        dto.setPassword(password);
        return dto;
    }

    public static UserPatchDTO patchWithEmailAndPassword(String email, String password) {
        UserPatchDTO dto = new UserPatchDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        return dto;
    }

    private static String generateRandomEmail() {
        return "email_" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
    }
}