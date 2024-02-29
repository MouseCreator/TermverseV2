package mouse.project.termverseweb.lib.service.provider;


import mouse.project.termverseweb.lib.service.helper.CrudHelper;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;

public interface CrudServiceProvider extends ServiceProvider {
    <MODEL, ID> CrudHelper<MODEL, ID> with(CustomCrudRepository<MODEL, ID> model);
}
