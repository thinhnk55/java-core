package work.vietdefi.clean.services.user;

import com.google.gson.JsonObject;
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

    /**
     * Set up resources before all tests.
     * Initialize the SQL bridge and create an instance of UserService.
     */
    @BeforeAll
    static void setup() throws IOException {
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
     * Verify that a user can be successfully registered.
     */
    @Test
    void testRegisterUser() {
        String username = "testUser";
        String password = "password123";

        JsonObject response = userService.register(username, password);

        assertNotNull(response);
        assertTrue(SimpleResponse.isSuccess(response));
        JsonObject data = response.getAsJsonObject("d");
        assertEquals(username, data.get("username").getAsString());
        assertNotNull(data.get("token").getAsString());
    }

    /**
     * Test user login.
     * Verify that a user with valid credentials can log in.
     */
    @Test
    void testLoginUser() {
        String username = "testUser";
        String password = "password123";
        userService.register(username, password);

        JsonObject response = userService.login(username, password);

        assertNotNull(response);
        assertTrue(SimpleResponse.isSuccess(response));
        JsonObject data = response.getAsJsonObject("d");
        assertEquals(username, data.get("username").getAsString());
        assertNotNull(data.get("token").getAsString());
    }

    /**
     * Test token authorization.
     * Verify that a valid token allows authorization.
     */
    @Test
    void testAuthorization() {
        String username = "testUser";
        String password = "password123";
        JsonObject loginResponse = userService.login(username, password);
        String token = loginResponse.getAsJsonObject("d").get("token").getAsString();

        JsonObject authResponse = userService.authorization(token);
        assertTrue(SimpleResponse.isSuccess(authResponse));

        JsonObject fakeResponse = userService.authorization("fake_token");
        assertFalse(SimpleResponse.isSuccess(fakeResponse));
    }
}
