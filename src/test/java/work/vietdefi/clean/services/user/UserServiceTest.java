package work.vietdefi.clean.services.user;

import com.google.gson.JsonObject;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.jupiter.api.*;
import work.vietdefi.clean.services.common.SimpleResponse;
import work.vietdefi.sql.HikariClient;
import work.vietdefi.sql.ISQLJavaBridge;
import work.vietdefi.sql.SqlJavaBridge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserService.
 * Tests are run against the 'test_user' table, which is dropped after tests complete.
 */
public class UserServiceTest {

    private static ISQLJavaBridge bridge;
    private static UserService userService;
    private static final String TEST_TABLE = "test_user";
    private static final String USER_NAME = "test_user";
    private static final String PASSWORD = "123456";
    private static String TOKEN;

    /**
     * Set up resources before all tests.
     * Initialize the SQL bridge and create an instance of UserService.
     */
    @BeforeAll
    static void setup() throws IOException {
        // Configure log4j using the specified XML configuration file
        DOMConfigurator.configure("config/log/log4j.xml");
        HikariClient hikariClient = new HikariClient("config/sql/databases.json");
        bridge = new SqlJavaBridge(hikariClient); // Create an instance of SqlJavaBridge
        // Initialize the UserService with the test table.
        userService = new UserService(bridge, TEST_TABLE);
    }

    /**
     * Clean up resources after all tests.
     * Drop the 'test_user' table to ensure a clean slate.
     */
    @AfterAll
    static void teardown() throws Exception {
        bridge.update("DROP TABLE IF EXISTS " + TEST_TABLE);
    }

    /**
     * Test user registration.
     * This test verifies the behavior of the registration process,
     * ensuring both successful and duplicate user registrations are handled correctly.
     */
    @Test
    @Order(1)
    void testRegisterUser() {
        // Attempt to register a new user.
        // Response returns e = 0 when the user is registered successfully.
        JsonObject response = userService.register(USER_NAME, PASSWORD);
        assertNotNull(response); // Ensure the response is not null.

        int error = response.get("e").getAsInt(); // Extract the error code.
        assertEquals(0, error); // Verify that the error code indicates success (e = 0).

        JsonObject data = response.getAsJsonObject("d"); // Extract the data object from the response.
        assertEquals(USER_NAME, data.get("username").getAsString()); // Check the registered username.
        assertNotNull(data.get("token").getAsString()); // Verify that a token is returned.
        TOKEN = data.get("token").getAsString(); // Store the token for use in subsequent test attempts.

        // Attempt to register the same user again.
        // Response returns e = 10 when a duplicate user registration is detected.
        response = userService.register(USER_NAME, PASSWORD);
        assertNotNull(response); // Ensure the response is not null.

        error = response.get("e").getAsInt(); // Extract the error code.
        assertEquals(10, error); // Verify that the error code indicates a duplicate user (e = 10).
    }


    /**
     * Test user login.
     * This test verifies that a user with valid credentials can log in successfully.
     * It also checks that appropriate error codes are returned for invalid login attempts.
     */
    @Test
    @Order(2)
    void testLoginUser() {
        // Attempt to log in with valid credentials.
        JsonObject response = userService.login(USER_NAME, PASSWORD);
        assertNotNull(response); // Ensure the response is not null.
        assertTrue(SimpleResponse.isSuccess(response)); // Check if the login was successful.

        JsonObject data = response.getAsJsonObject("d"); // Extract the data object.
        assertEquals(USER_NAME, data.get("username").getAsString()); // Verify the username.
        assertNotNull(data.get("token").getAsString()); // Ensure a token is returned.

        // Attempt to log in with an invalid username.
        response = userService.login("wrongUsername", PASSWORD);
        assertNotNull(response); // Ensure the response is not null.
        int error = response.get("e").getAsInt(); // Extract the error code.
        assertEquals(10, error); // Verify that the error code indicates an invalid username (e = 10).

        // Attempt to log in with an invalid password.
        response = userService.login(USER_NAME, "wrongPassword");
        assertNotNull(response); // Ensure the response is not null.
        error = response.get("e").getAsInt(); // Extract the error code.
        assertEquals(11, error); // Verify that the error code indicates an invalid password (e = 11).
    }


    /**
     * Test token authorization.
     * This test ensures that:
     * 1. A valid token grants access by returning a successful response.
     * 2. An invalid token is rejected with the appropriate error code.
     *
     * Expected Results:
     * - Success: e = 0 (valid token)
     * - Failure: e = 10 (invalid token)
     */
    @Test
    @Order(3) // Ensure this test runs after registration and login tests
    void testAuthorization() {
        // Attempt authorization with a valid token.
        JsonObject authResponse = userService.authorization(TOKEN);

        // Check if the response indicates a successful authorization (e = 0).
        assertTrue(SimpleResponse.isSuccess(authResponse));

        // Attempt authorization with an invalid (fake) token.
        JsonObject fakeResponse = userService.authorization("fake_token");

        // Extract the error code from the response.
        int error = fakeResponse.get("e").getAsInt();

        // Verify that the error code indicates an invalid token (e = 10).
        assertEquals(10, error);
    }
}
