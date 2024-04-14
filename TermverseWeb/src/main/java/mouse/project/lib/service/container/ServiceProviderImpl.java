package mouse.project.lib.service.container;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.service.GenericServiceHelper;
import mouse.project.lib.service.StatefulRepositoryCaller;
import mouse.project.lib.service.helper.CrudHelper;
import mouse.project.lib.service.helper.SoftDeleteHelper;
import mouse.project.lib.service.model.IdIterable;
import mouse.project.lib.service.provider.CrudServiceProvider;
import mouse.project.lib.service.provider.ServiceProvider;
import mouse.project.lib.service.provider.SoftDeleteProvider;
import mouse.project.lib.service.repository.CustomCrudRepository;
import mouse.project.lib.service.repository.GenericRepository;
import mouse.project.lib.service.repository.SoftDeleteRepository;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class ServiceProviderImpl implements ServiceProviderContainer {

    private final List<ServiceProvider> serviceProviderList;
    private final GenericServiceHelper genericServiceHelper;
    @Auto
    public ServiceProviderImpl(@Collect(ServiceProvider.class) List<ServiceProvider> serviceProviderList, GenericServiceHelper genericServiceHelper) {
        this.serviceProviderList = serviceProviderList;
        this.genericServiceHelper = genericServiceHelper;
    }

    @Override
    public <MODEL extends IdIterable<ID>, ID> CrudHelper<MODEL, ID> crud(CustomCrudRepository<MODEL, ID> crudRepository) {
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

    public <T extends ServiceProvider> T require(Class<T> interfaceClass) {
        return find(interfaceClass);
    }
}
