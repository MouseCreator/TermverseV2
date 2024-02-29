package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserUpdateDTO;
import mouse.project.termverseweb.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper extends ModelMapper<User, UserCreateDTO, UserUpdateDTO, UserResponseDTO> {

}
