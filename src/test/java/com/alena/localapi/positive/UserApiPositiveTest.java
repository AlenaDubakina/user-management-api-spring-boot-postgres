package com.alena.localapi.positive;

import com.alena.localapi.base.BaseTest;
import com.alena.localapi.constants.ApiEndpoints;
import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.alena.localapi.assertions.UserAssertions.*;

public class UserApiPositiveTest extends BaseTest {
    private static Stream<Arguments> validUser() {
        return Stream.of(
                Arguments.of("test1@mail.com", "12345"),
                Arguments.of("test2@yandex.com", "test2Password"),
                Arguments.of("test3@google.com", "test3Password"));
    }

    @ParameterizedTest
    @MethodSource("validUser")
    public void createUser(String email, String password) {
        UserRequestDTO userRequestDTO = new UserRequestDTO(email, password);

        UserResponseDTO user = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        assertUserHasRequiredFields(user);
        assertUserEmailIsEquals(user, userRequestDTO.getEmail());
    }

    @Test
    public void getAllUsers() {
        List<UserResponseDTO> users = testClient.get(ApiEndpoints.USERS)
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
        UserRequestDTO userRequestDTO = new UserRequestDTO("test4@mail.com", "123456565");

        UserResponseDTO createdUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserResponseDTO actualUser = testClient.getById(ApiEndpoints.USERS_BY_ID, createdUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserHasRequiredFields(actualUser);
        assertUserEquals(actualUser, createdUser);
    }

    @Test
    public void deleteUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("test5@mail.com", "123456password");

        UserResponseDTO savedUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        Long id = savedUser.getId();

        testClient.delete(ApiEndpoints.USERS_BY_ID, id)
                .then()
                .statusCode(204);

        testClient.getById(ApiEndpoints.USERS_BY_ID, id)
                .then()
                .statusCode(404);
    }

    @Test
    public void updateUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testPut@mail.com", "123456PasswordPut");

        UserResponseDTO user = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        userRequestDTO.setEmail("updateTest@google.com");

        UserResponseDTO updatedUser = testClient.put(ApiEndpoints.USERS_BY_ID, user.getId(), userRequestDTO)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updatedUser, userRequestDTO.getEmail());
    }

    @Test
    public void patchUser_email() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO createUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail("testPatchUpdate@google.com");

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, createUser.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, userPatchDTO.getEmail());
    }

    @Test
    public void patchUser_password() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO createUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setPassword("newPasswordPathTest");

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, createUser.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, userRequestDTO.getEmail());
    }

    @Test
    public void patchUpdate_emailAndPasswordUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO createUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail("testPatchUpdateEmailandPassword@google.com");
        userPatchDTO.setPassword("newPasswordPathTest");

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, createUser.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, userPatchDTO.getEmail());
    }

    @Test
    public void patchUpdate_notEmailPassword_shouldNotChangeUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO createUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, createUser.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEmailIsEquals(updateUser, createUser.getEmail());
    }

    @Test
    public void patchUser_nullEmail_shouldBeIgnored() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testCreate_" + System.currentTimeMillis() + "@mail.com", "123456PasswordPut");

        UserResponseDTO createUser = testClient.post(ApiEndpoints.USERS, userRequestDTO)
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        UserPatchDTO userPatchDTO = new UserPatchDTO();
        userPatchDTO.setEmail(null);

        UserResponseDTO updateUser = testClient.patch(ApiEndpoints.USERS_BY_ID, createUser.getId(), userPatchDTO)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertUserEquals(updateUser, createUser);
    }
}