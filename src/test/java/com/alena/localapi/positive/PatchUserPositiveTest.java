package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static com.alena.localapi.assertions.UserAssertions.assertUserEmailIsEquals;
import static com.alena.localapi.assertions.UserAssertions.assertUserEquals;
import static com.alena.localapi.factory.UserFactory.*;

public class PatchUserPositiveTest extends BaseTest {
    @Test
    public void patchUser_email() {
        UserResponseDTO user = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithEmail("testPatchUpdate@google.com");

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, user.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, userPatchDTO.getEmail());
    }

    @Test
    public void patchUser_password() {
        UserResponseDTO user = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithPassword("newPasswordPathTest");

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, user.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, user.getEmail());
    }

    @Test
    public void patchUpdate_emailAndPasswordUser() {
        UserResponseDTO user = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithEmailAndPassword("testPatchUpdateEmailandPassword@google.com", "newPasswordPathTest");

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, user.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, userPatchDTO.getEmail());
    }

    @Test
    public void patchUpdate_notEmailPassword_shouldNotChangeUser() {
        UserResponseDTO user = createUser(defaultUser());

        UserPatchDTO userPatchDTO = new UserPatchDTO();

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, user.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, user.getEmail());
    }

    @Test
    public void patchUser_nullEmail_shouldBeIgnored() {
        UserResponseDTO user = createUser(defaultUser());

        UserPatchDTO userPatchDTO = patchWithEmail(null);

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, user.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, user);
    }
}