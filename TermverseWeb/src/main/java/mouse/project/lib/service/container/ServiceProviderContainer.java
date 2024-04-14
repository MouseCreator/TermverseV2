package mouse.project.lib.service.container;


import mouse.project.lib.service.StatefulRepositoryCaller;
import mouse.project.lib.service.helper.CrudHelper;
import mouse.project.lib.service.helper.SoftDeleteHelper;
import mouse.project.lib.service.model.IdIterable;
import mouse.project.lib.service.provider.ServiceProvider;
import mouse.project.lib.service.repository.CustomCrudRepository;
import mouse.project.lib.service.repository.GenericRepository;
import mouse.project.lib.service.repository.SoftDeleteRepository;

public interface ServiceProviderContainer {
    <MODEL extends IdIterable<ID>, ID> CrudHelper<MODEL, ID> crud(CustomCrudRepository<MODEL, ID> crudRepository);
    <MODEL, ID> SoftDeleteHelper<MODEL, ID> soft(SoftDeleteRepository<MODEL, ID> softDeleteRepository);
    <REPOSITORY extends GenericRepository> StatefulRepositoryCaller<REPOSITORY> use(REPOSITORY repository);
    <T extends ServiceProvider> T require(Class<T> interfaceClass);
}
