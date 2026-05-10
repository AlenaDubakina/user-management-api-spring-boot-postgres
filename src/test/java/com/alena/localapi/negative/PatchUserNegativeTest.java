package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.alena.localapi.assertions.ErrorAssertions.*;
import static com.alena.localapi.assertions.UserAssertions.assertUserEquals;
import static com.alena.localapi.factory.UserFactory.*;

public class PatchUserNegativeTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("com.alena.localapi.providers.user.PatchUserDataProvider#invalidEmailForPatch")
    public void patchUser_email_negative(String email, List<String> expectedFields) {

        UserResponseDTO savedUser = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithEmail(email);

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
    @MethodSource("com.alena.localapi.providers.user.PatchUserDataProvider#invalidPasswordForPatch")
    public void patchUser_password_negative(String password, List<String> expectedFields) {
        UserResponseDTO savedUser = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithPassword(password);

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
    @MethodSource("com.alena.localapi.providers.user.PatchUserDataProvider#invalidMultipleFieldsFailFast")
    public void patchUser_emailPassword_negative(String email, String password, List<String> expectedFields) {
        UserResponseDTO savedUser = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithEmailAndPassword(email, password);

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
        UserResponseDTO savedUser = createUser(defaultUser());

        UserResponseDTO savedNewUser = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithEmail(savedUser.getEmail());

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