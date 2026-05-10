package com.alena.localapi.providers.user;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class PatchUserDataProvider {
    public static Stream<Arguments> invalidEmailForPatch() {
        return Stream.of(Arguments.of("", List.of("email")),
                Arguments.of("invalid@mail", List.of("email")),
                Arguments.of(" ", List.of("email")));
    }

    public static Stream<Arguments> invalidPasswordForPatch() {
        return Stream.of(Arguments.of("", List.of("password")),
                Arguments.of(" ", List.of("password")));
    }

    public static Stream<Arguments> invalidMultipleFieldsFailFast() {
        return Stream.of(Arguments.of("", null, List.of("email")),
                Arguments.of(null, "", List.of("password")),
                Arguments.of("incorrect@mail", null, List.of("email")),
                Arguments.of(" ", null, List.of("email")),
                Arguments.of(null, " ", List.of("password")),
                Arguments.of("", "", List.of("email")),
                Arguments.of(" ", " ", List.of("email")));
    }
}