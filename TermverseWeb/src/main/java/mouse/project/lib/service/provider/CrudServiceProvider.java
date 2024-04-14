package mouse.project.lib.service.provider;


import mouse.project.lib.service.helper.CrudHelper;
import mouse.project.lib.service.model.IdIterable;
import mouse.project.lib.service.repository.CustomCrudRepository;

public interface CrudServiceProvider extends ServiceProvider {
    <MODEL extends IdIterable<ID>, ID> CrudHelper<MODEL, ID> with(CustomCrudRepository<MODEL, ID> model);
}
