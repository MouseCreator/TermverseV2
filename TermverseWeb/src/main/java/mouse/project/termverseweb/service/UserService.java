package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.UserResponseDTO;
import mouse.project.termverseweb.model.User;

import java.util.List;


public interface UserService {
    UserResponseDTO save(User model);
    List<UserResponseDTO> findAll();
    UserResponseDTO getById(Long id);
    UserResponseDTO update(User model);
    void removeById(Long id);
    UserResponseDTO hardGet(Long id);
    void restoreById(Long id);
    List<UserResponseDTO> findAllWithDeleted();
    List<UserResponseDTO> findByName(String name);
}
