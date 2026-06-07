package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrorResponse;
import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrors;
import static com.alena.localapi.assertions.UserAssertions.assertUserEquals;
import static com.alena.localapi.factory.UserFactory.customUser;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class UpdateUserNegativeTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.UserDataProvider#invalidUserData")
    public void updateUser_negative(String email, String password, List<String> expectedFields) {
        String token = getAuthToken();

        UserResponseDTO savedUser = createUser(defaultUser(), token);

        UserRequestDTO userRequestDTO = customUser(email, password);

        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, savedUser.getId(), userRequestDTO, token)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrors(errorResponseDTO,
                400,
                "Validation failed",
                "Bad Request",
                ApiEndpoints.USERS,
                expectedFields);
    }

    @Test
    public void updateUser_noExists_negative() {
        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, 999L, defaultUser(), getAuthToken())
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404, "Пользователь с таким id %d не существует"
                .formatted(999L), "Not Found", ApiEndpoints.USERS);
    }

    @Test
    public void updateUser_alreadyExistsEmail_negative() {
        String token = getAuthToken();

        UserResponseDTO savedUser = createUser(defaultUser(), token);

        UserRequestDTO newUserRequestDTO = defaultUser();

        UserResponseDTO savedNewUser = createUser(newUserRequestDTO, token);

        newUserRequestDTO.setEmail(savedUser.getEmail());

        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, savedNewUser.getId(), newUserRequestDTO, token)
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 409, "Пользователь с таким email %s уже существует"
                .formatted(newUserRequestDTO.getEmail()), "Conflict", ApiEndpoints.USERS);

        UserResponseDTO updateUser = testClient.getById(ApiEndpoints.USERS_BY_ID, savedNewUser.getId(), token)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, savedNewUser);
    }
}