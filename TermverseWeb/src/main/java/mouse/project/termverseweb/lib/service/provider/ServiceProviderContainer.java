package mouse.project.termverseweb.lib.service.provider;

import mouse.project.termverseweb.lib.service.StatefulRepositoryCaller;
import mouse.project.termverseweb.lib.service.helper.CrudHelper;
import mouse.project.termverseweb.lib.service.helper.SoftDeleteHelper;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import mouse.project.termverseweb.lib.service.repository.SoftDeleteRepository;

public interface ServiceProviderContainer {
    <MODEL, ID> CrudHelper<MODEL, ID> crud(CustomCrudRepository<MODEL, ID> crudRepository);
    <MODEL, ID> SoftDeleteHelper<MODEL, ID> soft(SoftDeleteRepository<MODEL, ID> softDeleteRepository);
    <REPOSITORY extends GenericRepository> StatefulRepositoryCaller<REPOSITORY> use(REPOSITORY repository);
}
