package mouse.project.termverseweb.service.register;

import mouse.project.termverseweb.dto.register.UserRegisterDTO;

import javax.naming.OperationNotSupportedException;

public class KeycloakMockImplementation implements KeycloakClient {
    @Override
    public Tokens registerUser(UserRegisterDTO userRegisterDTO) throws Exception {
        return new Tokens(
                userRegisterDTO.getLogin() + "-acc",
                userRegisterDTO.getLogin() + "-ref"
        );
    }

    @Override
    public Tokens loginUser(UserRegisterDTO userRegisterDTO) throws Exception {
        return new Tokens(
                userRegisterDTO.getLogin() + "-acc",
                userRegisterDTO.getLogin() + "-ref"
        );
    }

    @Override
    public String fetchKeycloakKeys() throws Exception {
        throw new OperationNotSupportedException();
    }
}
