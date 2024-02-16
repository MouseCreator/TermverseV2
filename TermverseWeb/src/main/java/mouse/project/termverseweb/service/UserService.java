package mouse.project.termverseweb.service;

import mouse.project.termverseweb.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends CrudService<User, Long> {
    List<User> getByNameIgnoreCase(String name);

}
