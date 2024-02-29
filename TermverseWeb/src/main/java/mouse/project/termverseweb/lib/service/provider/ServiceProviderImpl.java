package mouse.project.termverseweb.lib.service.provider;

import mouse.project.termverseweb.lib.service.GenericServiceHelper;
import mouse.project.termverseweb.lib.service.StatefulRepositoryCaller;
import mouse.project.termverseweb.lib.service.helper.*;
import mouse.project.termverseweb.lib.service.helper.ServiceProvider;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import mouse.project.termverseweb.lib.service.repository.SoftDeleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class ServiceProviderImpl implements ServiceProviderContainer {

    private final List<ServiceProvider> serviceProviderList;
    private final GenericServiceHelper genericServiceHelper;
    @Autowired
    public ServiceProviderImpl(List<ServiceProvider> serviceProviderList, GenericServiceHelper genericServiceHelper) {
        this.serviceProviderList = serviceProviderList;
        this.genericServiceHelper = genericServiceHelper;
    }

    @Override
    public <MODEL, ID> CrudHelper<MODEL, ID> crud(CustomCrudRepository<MODEL, ID> crudRepository) {
        return find(CrudServiceProvider.class).with(crudRepository);
    }

    @Override
    public <MODEL, ID> SoftDeleteHelper<MODEL, ID> soft(SoftDeleteRepository<MODEL, ID> softDeleteRepository) {
        return find(SoftDeleteProvider.class).with(softDeleteRepository);
    }

    @Override
    public <REPOSITORY extends GenericRepository> StatefulRepositoryCaller<REPOSITORY> use(REPOSITORY repository) {
        return genericServiceHelper.use(repository);
    }

    private <T extends ServiceProvider> T find(Class<T> interfaceClass) {
        for (ServiceProvider serviceProvider : serviceProviderList) {
            if (interfaceClass.isAssignableFrom(serviceProvider.getClass())) {
                return interfaceClass.cast(serviceProvider);
            }
        }
        throw new NoSuchElementException("Cannot find implementation for provider interface " + interfaceClass.getName());
    }
}
