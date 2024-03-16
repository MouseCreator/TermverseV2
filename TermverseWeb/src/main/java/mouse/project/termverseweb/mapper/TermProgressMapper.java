package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.progress.TermProgressResponseDTO;
import mouse.project.termverseweb.dto.progress.TermProgressUpdateDTO;
import mouse.project.termverseweb.model.UserTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {TermMapper.class, UserMapper.class})
public interface TermProgressMapper {
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToId")
    @Mapping(source = "term", target = "termId", qualifiedByName = "termToId")
    TermProgressResponseDTO toResponse(UserTerm userStudySet);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userById")
    @Mapping(source = "termId", target = "term", qualifiedByName = "termFromId")
    UserTerm fromUpdate(TermProgressUpdateDTO userStudySet);

}
