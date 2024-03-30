package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import org.springframework.stereotype.Service;

@Service
public class TagFactory implements Factory{
    public TagCreateDTO tagCreateDTO(Long owner, String name) {
        TagCreateDTO createDTO = new TagCreateDTO();
        createDTO.setName(name);
        createDTO.setOwnerId(owner);
        createDTO.setColorHex("#FF0000");
        return createDTO;
    }
}
