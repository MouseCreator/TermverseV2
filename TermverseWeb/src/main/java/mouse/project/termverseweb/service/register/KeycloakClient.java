package mouse.project.termverseweb.service.register;

import mouse.project.termverseweb.dto.register.UserRegisterDTO;


public interface KeycloakClient {
    Tokens registerUser(UserRegisterDTO userRegisterDTO) throws Exception;
    Tokens loginUser(UserRegisterDTO userRegisterDTO) throws Exception;
    String fetchKeycloakKeys() throws Exception;
}

