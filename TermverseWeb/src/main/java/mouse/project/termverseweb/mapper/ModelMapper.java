package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.model.User;

public interface ModelMapper<MODEL, CREATE, UPDATE, RESPONSE> {
    RESPONSE toResponse(User user);
    MODEL fromCreate(CREATE userCreateDTO);
    MODEL fromUpdate(UPDATE userCreateDTO);
}
