package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.UserResponseDTO;
import mouse.project.termverseweb.exception.UpdateException;
import mouse.project.termverseweb.mapper.Mapper;
import mouse.project.termverseweb.mapper.UserMapper;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    @Override
    public UserResponseDTO save(User model) {
        User savedUser = userRepository.save(model);
        return userMapper.toResponse(savedUser);
    }
    @Override
    public List<UserResponseDTO> findAll() {
        List<User> models = userRepository.findAll();
        return Mapper.mapAll(models).toAndGet(userMapper::toResponse);
    }
    @Override
    public UserResponseDTO getById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new NoSuchElementException("Cannot find user by id: " + id));
        return Mapper.transform(user, userMapper::toResponse);
    }

    @Override
    public UserResponseDTO updateById(User model, Long id) {
        if (model.getId() == null) {
            model.setId(id);
        } else if (!model.getId().equals(id)) {
            throw new UpdateException("Updating user with wrong ID. ID mismatch: " + model.getId() + ", " + id);
        }
        User savedModel = userRepository.save(model);
        return Mapper.transform(savedModel, userMapper::toResponse);
    }

    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public List<User> getByNameIgnoreCase(String name) {
        return userRepository.findAllByNameIgnoreCase(name);
    }
}
