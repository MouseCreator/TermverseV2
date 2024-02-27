package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.UserResponseDTO;
import mouse.project.termverseweb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    UserResponseDTO save(User model);
    List<UserResponseDTO> findAll();
    UserResponseDTO getById(Long id);
    UserResponseDTO updateById(User model, Long id);
    void removeById(Long id);
    List<User> getByNameIgnoreCase(String name);
    List<User> getDeletedUsers();
    User hardGetUserById(Long id);

}
