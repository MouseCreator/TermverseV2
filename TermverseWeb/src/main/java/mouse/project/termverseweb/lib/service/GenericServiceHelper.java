package mouse.project.termverseweb.lib.service;


import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
@mouse.project.lib.ioc.annotation.Service
public class GenericServiceHelper {

    public <REPOSITORY extends GenericRepository>
    StatefulRepositoryCaller<REPOSITORY> use(REPOSITORY repository) {
        return new StatefulRepositoryCaller<>(repository);
    }

}
