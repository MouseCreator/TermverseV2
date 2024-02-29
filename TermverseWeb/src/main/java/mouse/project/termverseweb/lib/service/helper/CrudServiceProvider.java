package mouse.project.termverseweb.lib.service.helper;


import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;

public interface CrudServiceProvider extends ServiceProvider {
    <MODEL, ID> CrudHelper<MODEL, ID> with(CustomCrudRepository<MODEL, ID> model);
}
