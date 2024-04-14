package mouse.project.lib.service;


import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.service.repository.GenericRepository;

@Service
public class GenericServiceHelper {
    public <REPOSITORY extends GenericRepository>
    StatefulRepositoryCaller<REPOSITORY> use(REPOSITORY repository) {
        return new StatefulRepositoryCaller<>(repository);
    }

}
