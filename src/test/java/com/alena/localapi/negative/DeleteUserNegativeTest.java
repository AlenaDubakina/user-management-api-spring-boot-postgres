package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrorResponse;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class DeleteUserNegativeTest extends BaseTest {
    @Test
    public void deleteUser_noExists_negative() {
        String adminToken = getAdminToken();

        ErrorResponseDTO errorResponseDTO = testClient.delete(ApiEndpoints.USERS_BY_ID, 999L, adminToken)
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404,
                "Пользователь с таким id %d не существует".formatted(999L),
                "Not Found",
                ApiEndpoints.USERS);
    }

    @Test
    public void deleteUser_without_token_negative() {
        String adminToken = getAdminToken();

        UserResponseDTO savedUser = createUser(defaultUser(), adminToken);

        Long id = savedUser.getId();

        ErrorResponseDTO errorResponseDTO = testClient.delete(ApiEndpoints.USERS_BY_ID, id)
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO,
                401,
                "Full authentication is required to access this resource",
                "Unauthorized",
                ApiEndpoints.USERS);
    }

    @Test
    public void deleteUser_invalid_token_negative() {
        String adminToken = getAdminToken();

        UserResponseDTO savedUser = createUser(defaultUser(), adminToken);

        Long id = savedUser.getId();

        ErrorResponseDTO errorResponseDTO = testClient.delete(ApiEndpoints.USERS_BY_ID, id, "invalid.token")
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO,
                401,
                "Invalid JWT token",
                "Unauthorized",
                ApiEndpoints.USERS);
    }

    @Test
    public void deleteUser_userRole_forbidden() {
        String userToken = getUserToken();

        UserResponseDTO savedUser = createUser(defaultUser(), userToken);

        Long id = savedUser.getId();

        ErrorResponseDTO errorResponseDTO = testClient.delete(ApiEndpoints.USERS_BY_ID, id, userToken)
                .then()
                .statusCode(403)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO,
                403,
                "Access Denied",
                "Forbidden",
                ApiEndpoints.USERS);
    }
}