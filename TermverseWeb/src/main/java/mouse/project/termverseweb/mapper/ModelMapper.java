package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.dto.genetic.CreateDTO;
import mouse.project.termverseweb.dto.genetic.ResponseDTO;
import mouse.project.termverseweb.dto.genetic.UpdateDTO;

public interface ModelMapper<MODEL, CREATE extends CreateDTO<MODEL>,
        UPDATE extends UpdateDTO<MODEL>, RESPONSE extends ResponseDTO<MODEL>> {
    RESPONSE toResponse(MODEL model);
    MODEL fromCreate(CREATE createDTO);
    MODEL fromUpdate(UPDATE updateDTO);
}
