package mouse.project.termverseweb.filters.argument;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionalAuthorizationFactoryTest {

    private OptionalAuthorizationFactory factory;

    @BeforeEach
    void setUp() {
        factory = new OptionalAuthorizationFactory();
    }

    @Test
    void processGoodResponse() {
        OptionalAuthorization optionalAuthorization = factory.processTokenResponse("""
                {
                    "username": "John",
                    "databaseId": 3
                }
                """);
        Long userDatabaseId = optionalAuthorization.getUserDatabaseId();
        String username = optionalAuthorization.getUsername();
        assertEquals(3, userDatabaseId);
        assertEquals("John", username);
    }

    @Test
    void processBadResponse() {
        OptionalAuthorization optionalAuthorization = factory.processTokenResponse("""
                {
                    "active": "false"
                }
                """);
        assertTrue(optionalAuthorization.isEmpty());
    }
}