package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.UserResponseDTO;
import mouse.project.termverseweb.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserServiceImplTest {

    private final UserService userService;
    @Autowired
    public UserServiceImplTest(UserService userService) {
        this.userService = userService;
    }


    @Test
    void save() {
        User user = new User();
        user.setName("Michael");
        user.setProfilePictureUrl("https://michael/profile.image.jpg");
        UserResponseDTO savedUser = userService.save(user);
        Assertions.assertNotNull(savedUser.getId());
        Long id = savedUser.getId();

        UserResponseDTO gotUser = userService.getById(id);
        Assertions.assertEquals(user.getName(), gotUser.getName());
        Assertions.assertEquals(user.getProfilePictureUrl(), gotUser.getProfilePictureUrl());
    }

    @Test
    void findAll() {
    }
}