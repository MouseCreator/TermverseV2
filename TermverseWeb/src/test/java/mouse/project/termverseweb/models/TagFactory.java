package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.model.Tag;
import mouse.project.termverseweb.model.User;
import org.springframework.stereotype.Service;

@Service
@mouse.project.lib.ioc.annotation.Service
public class TagFactory implements Factory{
    public TagCreateDTO tagCreateDTO(Long owner, String name) {
        TagCreateDTO createDTO = new TagCreateDTO();
        createDTO.setName(name);
        createDTO.setOwnerId(owner);
        createDTO.setColorHex("#FF0000");
        return createDTO;
    }

    public Tag tag(User owner, String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setOwner(owner);
        tag.setColorHex("#FF00FF");
        return tag;
    }
}
