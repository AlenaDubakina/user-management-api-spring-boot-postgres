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

import static com.alena.localapi.assertions.ErrorAssertions.*;
import static com.alena.localapi.assertions.UserAssertions.assertUserEquals;
import static com.alena.localapi.factory.UserFactory.customUser;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class UpdateUserNegativeTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.UserDataProvider#invalidUserData")
    public void updateUser_negative(String email, String password, List<String> expectedFields) {
        UserResponseDTO savedUser = createUser(defaultUser());

        UserRequestDTO userRequestDTO = customUser(email, password);

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
        ErrorResponseDTO errorResponseDTO = testClient.put(ApiEndpoints.USERS_BY_ID, 999L, defaultUser())
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404, "Пользователь с таким id %d не существует"
                .formatted(999L), "Not Found", ApiEndpoints.USERS);
    }

    @Test
    public void updateUser_alreadyExistsEmail_negative() {
        UserResponseDTO savedUser = createUser(defaultUser());

        UserRequestDTO newUserRequestDTO = defaultUser();

        UserResponseDTO savedNewUser = createUser(newUserRequestDTO);

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
}