package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

public class UserApiNegativeTest extends BaseTest {

    private static Stream<Arguments> invalidUsers() {
        return Stream.of(
                Arguments.of("test1@mail.com", "", "password"),
                Arguments.of("test2@mail.com", null, "password"),
                Arguments.of("", "12345", "email"),
                Arguments.of(null, "12345", "email"),
                Arguments.of("testnew@mail", "1234567", "email"));
    }

    @ParameterizedTest
    @MethodSource("invalidUsers")
    public void createInvalidUserNegativeTest(String email, String password, String expectedFieldError) {
        UserRequestDTO invalidUser = new UserRequestDTO(email, password);

        Map<String, String> errors = testClient.postRequest(ApiEndpoints.USERS, invalidUser)
                .then()
                .statusCode(400)
                .extract()
                .as(new TypeRef<Map<String, String>>() {});

        assertThat(errors)
                .as("Некорректное поле с ошибкой")
                .containsKey(expectedFieldError);
    }

    @Test
    public void createUserAlreadyExistsNegativeTest() {
        UserRequestDTO userAlreadyExists = new UserRequestDTO("email_" + System.currentTimeMillis() + "@mail.com", "alreadyexists");

        testClient.postRequest(ApiEndpoints.USERS, userAlreadyExists)
                .then()
                .statusCode(201);

        testClient.postRequest(ApiEndpoints.USERS, userAlreadyExists)
                .then()
                .statusCode(409)
                .body(containsString("Пользователь с таким email %s уже существует"
                        .formatted(userAlreadyExists.getEmail())));
    }

    @Test
    public void getUserNoExistsTest() {
        testClient.getById(ApiEndpoints.USERS_BY_ID, 999L)
                .then()
                .statusCode(404)
                .body(containsString("не существует"));
    }

    @Test
    public void deleteUserNoExistsTest() {
        testClient.delete(ApiEndpoints.USERS_BY_ID, 999L)
                .then()
                .statusCode(404)
                .body(containsString("не существует"));
    }
}