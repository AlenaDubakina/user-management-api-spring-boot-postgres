package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.alena.localapi.assertions.UserAssertions.*;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class GetUserPositiveTest extends BaseTest {
    @Test
    public void getAllUsers() {
        List<UserResponseDTO> users = testClient.get(ApiEndpoints.USERS, getAuthToken())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", UserResponseDTO.class);

        assertUsersIsNotEmpty(users);
        users.forEach(user -> assertUserHasRequiredFields(user));
    }

    @Test
    public void getUserById() {
        String token = getAuthToken();

        UserResponseDTO createdUser = createUser(defaultUser(), token);

        UserResponseDTO actualUser = testClient.getById(ApiEndpoints.USERS_BY_ID, createdUser.getId(), token)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserHasRequiredFields(actualUser);
        assertUserEquals(actualUser, createdUser);
    }
}