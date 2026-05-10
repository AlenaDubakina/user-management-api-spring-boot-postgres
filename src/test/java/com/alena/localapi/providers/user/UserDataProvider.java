package com.alena.localapi.providers.user;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class UserDataProvider {
    public static Stream<Arguments> validUserData() {
        return Stream.of(
                Arguments.of("test1@mail.com", "12345"),
                Arguments.of("test2@yandex.com", "test2Password"),
                Arguments.of("test3@google.com", "test3Password"));
    }

    public static Stream<Arguments> invalidUserData() {
        return Stream.of(
                Arguments.of("test1@mail.com", "", List.of("password")),
                Arguments.of("test2@mail.com", null, List.of("password")),
                Arguments.of("", "12345", List.of("email")),
                Arguments.of(null, "12345", List.of("email")),
                Arguments.of("testnew@mail", "1234567", List.of("email")),
                Arguments.of(null, null, List.of("email", "password")),
                Arguments.of("", "", List.of("email", "password")));
    }
}