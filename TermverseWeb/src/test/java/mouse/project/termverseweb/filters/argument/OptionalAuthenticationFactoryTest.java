package mouse.project.termverseweb.filters.argument;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionalAuthenticationFactoryTest {

    private OptionalAuthorizationFactory factory;

    @BeforeEach
    void setUp() {
        factory = new OptionalAuthorizationFactory();
    }

    @Test
    void processGoodResponse() {
        OptionalAuthentication optionalAuthentication = factory.processTokenResponse("""
                {
                    "sub:" "some-key"
                    "username": "John",
                }
                """);
        String securityId = optionalAuthentication.getSecurityId();
        assertEquals("some-key", securityId);
    }

    @Test
    void processBadResponse() {
        OptionalAuthentication optionalAuthentication = factory.processTokenResponse("""
                {
                    "active": "false"
                }
                """);
        assertTrue(optionalAuthentication.isEmpty());
    }
}