package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.assertions.UserAssertions.assertUserEmailIsEquals;
import static com.alena.localapi.factory.UserFactory.defaultUser;

public class UpdateUserPositiveTest extends BaseTest {
    @Test
    public void updateUser() {
        String userToken = getUserToken();

        UserRequestDTO userRequestDTO = defaultUser();

        UserResponseDTO user = createUser(userRequestDTO, userToken);

        userRequestDTO.setEmail("updateTest@google.com");

        UserResponseDTO updatedUser = testClient.put(ApiEndpoints.USERS_BY_ID, user.getId(), userRequestDTO, userToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updatedUser, userRequestDTO.getEmail());
    }
}