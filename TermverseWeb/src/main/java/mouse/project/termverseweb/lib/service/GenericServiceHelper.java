package mouse.project.termverseweb.lib.service;


import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class GenericServiceHelper {

    public <MODEL, ID, REPOSITORY extends GenericRepository<MODEL, ID>>
    StatefulRepositoryCaller<REPOSITORY, MODEL, ID> use(REPOSITORY repository) {
        return new StatefulRepositoryCaller<>(repository);
    }

}
