package mouse.project.termverseweb.service.register;

import mouse.project.termverseweb.dto.register.UserRegisterDTO;
import mouse.project.termverseweb.dto.register.UserTokensDTO;

public interface RegisterService {
    UserTokensDTO register(UserRegisterDTO registerDTO);
    UserTokensDTO login(UserRegisterDTO registerDTO);
}
