package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.user.UserCreateDTO;
import org.springframework.stereotype.Service;

@Service
public class UserFactory implements Factory {

    public UserCreateDTO userCreateDTO(String name) {
        UserCreateDTO user = new UserCreateDTO();
        user.setName(name);
        String urlName = name.toLowerCase();
        user.setProfilePictureUrl(String.format("https://%s/profile.image.jpg", urlName));
        return user;
    }
}
