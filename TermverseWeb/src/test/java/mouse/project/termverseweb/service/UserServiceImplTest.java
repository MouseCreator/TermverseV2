package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserUpdateDTO;
import mouse.project.termverseweb.exception.UpdateException;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.UserFactory;
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
    private final Factories factories;
    @Autowired
    public UserServiceImplTest(UserService userService, Factories factories) {
        this.userService = userService;
        this.factories = factories;
    }

    private UserCreateDTO generateUserWithName(String name) {
        return factories.getFactory(UserFactory.class).userCreateDTO(name);
    }

    @Test
    void save() {
        UserCreateDTO user = generateUserWithName("Michael");
        UserResponseDTO savedUser = userService.save(user);
        Assertions.assertNotNull(savedUser.getId());
        Long id = savedUser.getId();

        UserResponseDTO gotUser = userService.getById(id);
        Assertions.assertEquals(user.getName(), gotUser.getName());
        Assertions.assertEquals(user.getProfilePictureUrl(), gotUser.getProfilePictureUrl());
    }

    @Test
    void findAll() {
        UserCreateDTO user = generateUserWithName("Luis");
        UserResponseDTO userResponseDTO = userService.save(user);
        Assertions.assertFalse(userService.findAll().isEmpty());
        Assertions.assertTrue(userService.findAll().contains(userResponseDTO));
    }

    @Test
    void testSoftDelete() {
        UserCreateDTO user = generateUserWithName("Denis");
        UserResponseDTO savedUser = userService.save(user);
        Long id = savedUser.getId();
        userService.removeById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(id));
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.removeById(id));

        Assertions.assertFalse(userService.findAll().contains(savedUser));
        Assertions.assertTrue(userService.findAllWithDeleted().contains(savedUser));
    }

    @Test
    void testDeleteWithRestore() {
        UserCreateDTO lenny = generateUserWithName("Lenny");
        UserResponseDTO savedLenny = userService.save(lenny);
        Long id = savedLenny.getId();
        UserResponseDTO foundUser = userService.getById(id);
        String expectedName = lenny.getName();
        Assertions.assertEquals(expectedName, foundUser.getName());
        userService.removeById(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(id));
        UserResponseDTO deletedUser = userService.hardGet(id);
        Assertions.assertEquals(expectedName, deletedUser.getName());

        userService.restoreById(id);
        UserResponseDTO restoredUser = userService.getById(id);
        Assertions.assertEquals(expectedName, restoredUser.getName());
    }

    @Test
    void testSearchingByName() {
        UserCreateDTO perry = generateUserWithName("Perry");
        UserResponseDTO savedPerry = userService.save(perry);
        Long id = savedPerry.getId();
        Assertions.assertTrue(includesNameAndId("Perry", id));
        Assertions.assertTrue(includesNameAndId("PeR", id));
        Assertions.assertTrue(includesNameAndId("perry", id));
        Assertions.assertTrue(includesNameAndId(" perry ", id));

        Assertions.assertFalse(includesNameAndId("perrytto", id));
        Assertions.assertFalse(includesNameAndId("Per ry", id));
    }
    private boolean includesNameAndId(String name, Long id) {
        return userService.findByName(name)
                .stream()
                .anyMatch(p -> p.getId().equals(id));
    }

    @Test
    void testUpdate() {
        UserCreateDTO nikki = generateUserWithName("Nikki");

        UserUpdateDTO nikkiUpdate = new UserUpdateDTO();
        nikkiUpdate.setName(nikki.getName());
        nikkiUpdate.setProfilePictureUrl(null);

        Assertions.assertThrows(UpdateException.class, () -> userService.update(nikkiUpdate));

        UserResponseDTO savedNikki = userService.save(nikki);
        Long id = savedNikki.getId();
        nikkiUpdate.setId(id);

        UserResponseDTO updatedNikki = userService.update(nikkiUpdate);
        Assertions.assertNull(updatedNikki.getProfilePictureUrl());

        UserResponseDTO nikkiById = userService.getById(id);
        Assertions.assertNull(nikkiById.getProfilePictureUrl());


    }
}