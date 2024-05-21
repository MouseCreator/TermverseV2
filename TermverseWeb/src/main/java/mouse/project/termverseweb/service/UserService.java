package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserUpdateDTO;
import mouse.project.termverseweb.model.User;

import java.util.List;


public interface UserService {
    UserResponseDTO save(UserCreateDTO model);
    List<UserResponseDTO> findAll();
    UserResponseDTO getById(Long id);
    UserResponseDTO update(UserUpdateDTO model);
    void removeById(Long id);
    UserResponseDTO hardGet(Long id);
    void restoreById(Long id);
    List<UserResponseDTO> findAllWithDeleted();
    List<UserResponseDTO> findByName(String name);
    User saveUser(String login);
    boolean existsByName(String login);
}
