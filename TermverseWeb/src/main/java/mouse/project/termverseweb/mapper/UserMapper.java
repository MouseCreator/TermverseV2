package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.UserCreateDTO;
import mouse.project.termverseweb.dto.UserResponseDTO;
import mouse.project.termverseweb.dto.UserUpdateDTO;
import mouse.project.termverseweb.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDTO toResponse(User user);
    User toUser(UserCreateDTO userCreateDTO);
    User toUser(UserUpdateDTO userCreateDTO);
}
