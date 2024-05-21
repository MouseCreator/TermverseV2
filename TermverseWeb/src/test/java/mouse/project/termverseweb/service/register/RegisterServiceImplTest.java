package mouse.project.termverseweb.service.register;

import mouse.project.termverseweb.dto.register.UserRegisterDTO;
import mouse.project.termverseweb.dto.register.UserTokensDTO;
import mouse.project.termverseweb.exception.NoSuchUserException;
import mouse.project.termverseweb.exception.UserAlreadyExistsException;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.RegisterFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RegisterServiceImplTest {
    private final RegisterService registerService;
    private final Factories factories;
    @Autowired
    RegisterServiceImplTest(RegisterService registerService, Factories factories) {
        this.registerService = registerService;
        this.factories = factories;
    }


    @Test
    void register() {
        RegisterFactory factory = factories.getFactory(RegisterFactory.class);
        UserRegisterDTO userRegisterDTO = factory.registerDTO("User");
        UserTokensDTO tokens = registerService.register(userRegisterDTO);

        assertNotNull(tokens);
        assertNotNull(tokens.getAccessToken());
        assertNotNull(tokens.getRefreshToken());

        assertThrows(UserAlreadyExistsException.class, () -> registerService.register(userRegisterDTO));

    }

    @Test
    void login() {
        RegisterFactory factory = factories.getFactory(RegisterFactory.class);
        UserRegisterDTO notExistingUser = factory.registerDTO("__NOT_EXISTING__");

        assertThrows(NoSuchUserException.class,() -> registerService.login(notExistingUser));
        UserRegisterDTO userRegisterDTO = factory.registerDTO("User2");
        UserTokensDTO tokens = registerService.register(userRegisterDTO);
        UserTokensDTO tokens2 = registerService.login(userRegisterDTO);
        assertEquals(tokens, tokens2);

    }
}