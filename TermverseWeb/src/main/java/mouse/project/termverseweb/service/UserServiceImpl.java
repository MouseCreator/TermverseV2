package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserUpdateDTO;
import mouse.project.termverseweb.exception.UpdateException;
import mouse.project.termverseweb.mapper.Mapper;
import mouse.project.termverseweb.mapper.UserMapper;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public UserResponseDTO save(UserCreateDTO userCreateDTO) {
        User user = userMapper.fromCreate(userCreateDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
    @Override
    public List<UserResponseDTO> findAll() {
        List<User> models = userRepository.findAll();
        return toResponse(models);
    }
    @Override
    public UserResponseDTO getById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("Cannot find user by id: " + id));
        return Mapper.transform(user, userMapper::toResponse);
    }

    @Override
    public UserResponseDTO update(UserUpdateDTO userUpdateDTO) {
        User model = userMapper.fromUpdate(userUpdateDTO);
        if (model.getId()==null) {
            throw new UpdateException("Cannot update model without id: " + model);
        }
        User savedModel = userRepository.save(model);
        return Mapper.transform(savedModel, userMapper::toResponse);
    }

    @Override
    public void removeById(Long id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find user by id: " + id));
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO hardGet(Long id) {
        Optional<User> userOptional = userRepository.findByIdIncludeDeleted(id);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("Cannot find user by id: " + id));
        return Mapper.transform(user, userMapper::toResponse);
    }

    @Override
    public void restoreById(Long id) {
        userRepository.restoreById(id);
    }

    @Override
    public List<UserResponseDTO> findAllWithDeleted() {
        List<User> models = userRepository.findAllIncludeDeleted();
        return toResponse(models);
    }

    @Override
    public List<UserResponseDTO> findByName(String name) {
        String trimName = name.trim();
        List<User> models = userRepository.findAllByNameIgnoreCase(trimName);
        return toResponse(models);
    }

    private List<UserResponseDTO> toResponse(List<User> models) {
        return Mapper.mapAll(models).toAndGet(userMapper::toResponse);
    }
}
