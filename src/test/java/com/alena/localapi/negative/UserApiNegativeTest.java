package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.alena.localapi.assertions.ErrorAssertions.*;
import static com.alena.localapi.assertions.UserAssertions.assertUserEquals;

public class UserApiNegativeTest extends BaseTest {

    private static Stream<Arguments> invalidUserData() {
        return Stream.of(
                Arguments.of("test1@mail.com", "", List.of("password")),
                Arguments.of("test2@mail.com", null, List.of("password")),
                Arguments.of("", "12345", List.of("email")),
                Arguments.of(null, "12345", List.of("email")),
                Arguments.of("testnew@mail", "1234567", List.of("email")),
                Arguments.of(null, null, List.of("email", "password")),
                Arguments.of("", "", List.of("email", "password")));
    }

    private static Stream<Arguments> invalidEmailForPatch() {
        return Stream.of(Arguments.of("", List.of("email")),
                Arguments.of("invalid@mail", List.of("email")),
                Arguments.of(" ", List.of("email")));
    }

    private static Stream<Arguments> invalidPasswordForPatch() {
        return Stream.of(Arguments.of("", List.of("password")),
                Arguments.of(" ", List.of("password")));
    }

    private static Stream<Arguments> invalidMultipleFieldsFailFast() {
        return Stream.of(Arguments.of("", null, List.of("email")),
                Arguments.of(null, "", List.of("password")),
                Arguments.of("incorrect@mail", null, List.of("email")),
                Arguments.of(" ", null, List.of("email")),
                Arguments.of(null, " ", List.of("password")),
                Arguments.of("", "", List.of("email")),
                Arguments.of(" ", " ", List.of("email")));
    }

    @ParameterizedTest
    @MethodSource("invalidUserData")
    public void create_invalidUser_negative(String email, String password, List<String> expectedFields) {
        UserRequestDTO invalidUser = new UserRequestDTO(email, password);

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.USERS, invalidUser)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 400, "Validation failed", "Bad Request", ApiEndpoints.USERS);
        assertErrorsSize(errorResponseDTO.getErrors(), expectedFields.size());

        expectedFields.forEach(expectedFieldError ->
                assertFieldError(errorResponseDTO.getErrors(), expectedFieldError));
    }

    @Test
    public void createUser_alreadyExists_negative() {
        UserRequestDTO userAlreadyExists = new UserRequestDTO("email_" + System.currentTimeMillis() + "@mail.com", "alreadyexists");

        testClient.post(ApiEndpoints.USERS, userAlreadyExists)
                .then()
                .statusCode(201);

        ErrorResponseDTO errorResponseDTO = testClient.post(ApiEndpoints.USERS, userAlreadyExists)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 409, "Пользователь с таким email %s уже существует"
                .formatted(userAlreadyExists.getEmail()), "Conflict", ApiEndpoints.USERS);
    }

    @Test
    public void getUser_noExists_negative() {
        ErrorResponseDTO errorResponseDTO = testClient.getById(ApiEndpoints.USERS_BY_ID, 999L)
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404, "Пользователь с таким id %d не существует"
                .formatted(999L), "Not Found", ApiEndpoints.USERS);
    }

    @Test
    public void deleteUser_noExists_negative() {
        ErrorResponseDTO errorResponseDTO = testClient.delete(ApiEndpoints.USERS_BY_ID, 999L)
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404, "Пользователь с таким id %d не существует"
                .formatted(999L), "Not Found", ApiEndpoints.USERS);
    }

    @ParameterizedTest
    @MethodSource("invalidUserData")
    public void updateUser_negative(String email, String password, List<String> expectedFields) {
        UserRequestDTO userRequestDTO = new UserRequestDTO("test_" + System.currentTimeMillis() + "@mail.com", "123456Password");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        userRequestDTO.setEmail(email);
        userRequestDTO.setPassword(password);

        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, savedUser.getId(), userRequestDTO)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 400, "Validation failed", "Bad Request", ApiEndpoints.USERS);
        assertErrorsSize(errorResponseDTO.getErrors(), expectedFields.size());

        expectedFields.forEach(expectedFieldError ->
                assertFieldError(errorResponseDTO.getErrors(), expectedFieldError));
    }

    @Test
    public void updateUser_noExists_negative() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("user_" + System.currentTimeMillis() + "@mail.com", "123456Password");

        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, 999L, userRequestDTO)
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404, "Пользователь с таким id %d не существует"
                .formatted(999L), "Not Found", ApiEndpoints.USERS);
    }

    @Test
    public void updateUser_alreadyExistsEmail_negative() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserRequestDTO newUserRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedNewUser = testClient.post(ApiEndpoints.USERS, newUserRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        newUserRequestDTO.setEmail(savedUser.getEmail());

        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, savedNewUser.getId(), newUserRequestDTO)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 409, "Пользователь с таким email %s уже существует"
                .formatted(newUserRequestDTO.getEmail()), "Conflict", ApiEndpoints.USERS);

        UserResponseDTO updateUser = testClient.getById(ApiEndpoints.USERS_BY_ID, savedNewUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, savedNewUser);
    }

    @ParameterizedTest
    @MethodSource("invalidEmailForPatch")
    public void patchUser_email_negative(String email, List<String> expectedFields) {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail(email);

        ErrorResponseDTO errorResponseDTO = testClient.patch(ApiEndpoints.USERS_BY_ID, savedUser.getId(), userPatchDTO)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 400, "Validation failed", "Bad Request", ApiEndpoints.USERS);
        assertErrorsSize(errorResponseDTO.getErrors(), expectedFields.size());

        expectedFields.forEach(expectedFieldError ->
                assertFieldError(errorResponseDTO.getErrors(), expectedFieldError));

        UserResponseDTO updateUser = testClient.getById(ApiEndpoints.USERS_BY_ID, savedUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, savedUser);
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordForPatch")
    public void patchUser_password_negative(String password, List<String> expectedFields) {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setPassword(password);

        ErrorResponseDTO errorResponseDTO = testClient.patch(ApiEndpoints.USERS_BY_ID, savedUser.getId(), userPatchDTO)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 400, "Validation failed", "Bad Request", ApiEndpoints.USERS);
        assertErrorsSize(errorResponseDTO.getErrors(), expectedFields.size());

        expectedFields.forEach(expectedFieldError ->
                assertFieldError(errorResponseDTO.getErrors(), expectedFieldError));

        UserResponseDTO updateUser = testClient.getById(ApiEndpoints.USERS_BY_ID, savedUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, savedUser);
    }

    @ParameterizedTest
    @MethodSource("invalidMultipleFieldsFailFast")
    public void patchUser_emailPassword_negative(String email, String password, List<String> expectedFields) {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail(email);
        userPatchDTO.setPassword(password);

        ErrorResponseDTO errorResponseDTO = testClient.patch(ApiEndpoints.USERS_BY_ID, savedUser.getId(), userPatchDTO)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 400, "Validation failed", "Bad Request", ApiEndpoints.USERS);
        assertErrorsSize(errorResponseDTO.getErrors(), expectedFields.size());

        expectedFields.forEach(expectedFieldError ->
                assertFieldError(errorResponseDTO.getErrors(), expectedFieldError));

        UserResponseDTO updateUser = testClient.getById(ApiEndpoints.USERS_BY_ID, savedUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, savedUser);
    }

    @Test
    public void patchUser_alreadyExistsEmail_negative() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserRequestDTO newUserRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO savedNewUser = testClient.post(ApiEndpoints.USERS, newUserRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail(savedUser.getEmail());

        ErrorResponseDTO errorResponseDTO = testClient.patch(ApiEndpoints.USERS_BY_ID, savedNewUser.getId(), userPatchDTO)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 409, "Пользователь с таким email %s уже существует"
                .formatted(userPatchDTO.getEmail()), "Conflict", ApiEndpoints.USERS);

        UserResponseDTO updateUser = testClient.getById(ApiEndpoints.USERS_BY_ID, savedNewUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, savedNewUser);
    }
}