package mouse.project.termverseweb.filters.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import mouse.project.lib.files.manager.FileManager;
import mouse.project.lib.files.manager.FileManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class JWTDecoderTest {
    private JWTDecoder jwtDecoder;
    public static final String REALM = "termverse";
    public static final String BASE_DIR = "src/test/resources/data/keys/";

    private FileManager fileManager;
    @BeforeEach
    void setUp() {
        KeycloakInvoker invoker = Mockito.mock(KeycloakInvoker.class);
        fileManager = new FileManagerImpl();
        String json = fileManager.read(BASE_DIR + "keys_sample.json");
        Mockito.when(invoker.getKeycloakPublicKey(REALM)).thenReturn(
                json
        );
        jwtDecoder = new JWTDecoder(invoker);
    }

    @Test
    void decode() {
        PublicKey publicKey = jwtDecoder.getPublicKey(REALM);
        String jwtToken = fileManager.read(BASE_DIR + "keys_user.txt");
        Jws<Claims> decode = jwtDecoder.decode(jwtToken, publicKey);
        Claims payload = decode.getPayload();
        Long id = payload.get("databaseId", Long.class);
        assertEquals(3L, id);
        String subject = payload.getSubject();
        assertEquals("newuser", subject);
    }

    @Test
    void getPublicKey() {
        PublicKey publicKey = jwtDecoder.getPublicKey(REALM);
        String expected = fileManager.read( BASE_DIR + "keys_expected.txt");
        expected = expected.replaceAll("\\n", "\r\n");
        assertEquals(expected, publicKey.toString());
    }
}