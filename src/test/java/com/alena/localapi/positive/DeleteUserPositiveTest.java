package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.factory.UserFactory.defaultUser;

public class DeleteUserPositiveTest extends BaseTest {
    @Test
    public void deleteUser() {
        String token = getAuthToken();

        UserResponseDTO savedUser = createUser(defaultUser(), token);

        Long id = savedUser.getId();

        testClient.delete(ApiEndpoints.USERS_BY_ID, id, token)
                .then()
                .statusCode(204);

        testClient.getById(ApiEndpoints.USERS_BY_ID, id, token)
                .then()
                .statusCode(404);
    }
}