package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;

import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserUpdateDTO;
import mouse.project.termverseweb.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper  {
    UserResponseDTO toResponse(User model);
    User fromCreate(UserCreateDTO createDTO);
    User fromUpdate(UserUpdateDTO updateDTO);
    @Named("userById")
    default User getUserById(Long id) {
        return id == null ? null : new User(id);
    }
    @Named("userToId")
    default Long getUserId(User user) {
        return user == null ? null : user.getId();
    }
}
