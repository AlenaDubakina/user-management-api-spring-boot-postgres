package com.alena.localapi.negative;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.assertions.ErrorAssertions.assertValidationErrorResponse;

public class GetUserNegativeTest extends BaseTest {
    @Test
    public void getUser_without_token_negative() {
        testClient.get(ApiEndpoints.USERS)
                .then()
                .statusCode(403);
    }

    @Test
    public void getUser_invalid_token_negative() {
        testClient.get(ApiEndpoints.USERS, "invalid.jwt.token")
                .then()
                .statusCode(403);
    }

    @Test
    public void getUser_noExists_negative() {
        ErrorResponseDTO errorResponseDTO = testClient.getById(ApiEndpoints.USERS_BY_ID, 999L, getAuthToken())
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseDTO.class);

        assertValidationErrorResponse(errorResponseDTO, 404, "Пользователь с таким id %d не существует"
                .formatted(999L), "Not Found", ApiEndpoints.USERS);
    }
}