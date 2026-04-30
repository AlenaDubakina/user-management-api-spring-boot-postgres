package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.alena.localapi.assertions.UserAssertions.*;

public class UserApiPositiveTest extends BaseTest {
    private static Stream<Arguments> validUser() {
        return Stream.of(
                Arguments.of("test1@mail.com", "12345"),
                Arguments.of("test2@yandex.com", "test2Password"),
                Arguments.of("test3@google.com", "test3Password"));
    }

    @ParameterizedTest
    @MethodSource("validUser")
    public void createUserTest(String email, String password) {
        UserRequestDTO userRequestDTO = new UserRequestDTO(email, password);

        UserResponseDTO user = testClient.postRequest(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        assertUserHasRequiredFields(user);
        assertUserEmailIsEquals(user, userRequestDTO.getEmail());
    }

    @Test
    public void getAllUsersTest() {
        List<UserResponseDTO> users = testClient.getRequest(ApiEndpoints.USERS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", UserResponseDTO.class);

        assertUsersIsNotEmpty(users);
        users.forEach(user -> assertUserHasRequiredFields(user));
    }

    @Test
    public void getUserByIdTest() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("test4@mail.com", "123456565");

        UserResponseDTO createdUser = testClient.postRequest(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserResponseDTO actualUser = testClient.getById(ApiEndpoints.USERS_BY_ID, createdUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserHasRequiredFields(actualUser);
        assertUserEquals(actualUser, createdUser);
    }

    @Test
    public void deleteUserTest() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("test5@mail.com", "123456password");

        UserResponseDTO savedUser = testClient.postRequest(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        Long id = savedUser.getId();

        testClient.delete(ApiEndpoints.USERS_BY_ID, id)
                .then()
                .statusCode(204);

        testClient.getById(ApiEndpoints.USERS_BY_ID, id)
                .then()
                .statusCode(404);
    }
}