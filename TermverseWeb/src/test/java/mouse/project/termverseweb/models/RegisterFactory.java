package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.register.UserRegisterDTO;
@org.springframework.stereotype.Service
@mouse.project.lib.ioc.annotation.Service
public class RegisterFactory implements Factory{

    public UserRegisterDTO registerDTO(String login) {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setLogin(login);
        userRegisterDTO.setPassword(login + "123");
        return userRegisterDTO;
    }
}
