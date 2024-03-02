package mouse.project.termverseweb.lib.service.provider;


import mouse.project.termverseweb.lib.service.helper.CrudHelper;
import mouse.project.termverseweb.lib.service.model.IdIterable;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;

public interface CrudServiceProvider extends ServiceProvider {
    <MODEL extends IdIterable<ID>, ID> CrudHelper<MODEL, ID> with(CustomCrudRepository<MODEL, ID> model);
}
