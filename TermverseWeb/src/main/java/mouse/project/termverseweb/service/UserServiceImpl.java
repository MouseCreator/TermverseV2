package mouse.project.termverseweb.service;

import mouse.project.termverseweb.exception.UpdateException;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User model) {
        return userRepository.save(model);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateById(User model, Long id) {
        if (model.getId() == null) {
            model.setId(id);
        } else if (!model.getId().equals(id)) {
            throw new UpdateException("Updating user with wrong ID. ID mismatch: " + model.getId() + ", " + id);
        }
        return userRepository.save(model);
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
