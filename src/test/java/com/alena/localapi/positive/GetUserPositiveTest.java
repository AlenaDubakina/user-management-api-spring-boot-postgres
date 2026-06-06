package com.alena.localapi.positive;

import com.alena.localapi.auth.dto.AuthResponseDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.alena.localapi.assertions.UserAssertions.*;
import static com.alena.localapi.factory.AuthFactory.validRegisterRequest;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class GetUserPositiveTest extends BaseTest {
    @Test
    public void getAllUsers() {
        RegisterRequestDTO registerRequestDTO = validRegisterRequest();
        AuthResponseDTO authResponseDTO = registerUser(registerRequestDTO);

        List<UserResponseDTO> users = testClient.get(ApiEndpoints.USERS, authResponseDTO.getToken())
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
        UserResponseDTO createdUser = createUser(defaultUser());

        UserResponseDTO actualUser = testClient.getById(ApiEndpoints.USERS_BY_ID, createdUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserHasRequiredFields(actualUser);
        assertUserEquals(actualUser, createdUser);
    }
}