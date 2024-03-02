package mouse.project.termverseweb.lib.service.repository;


import mouse.project.termverseweb.lib.service.model.IdIterable;

public interface SoftDeleteCrudRepository<T extends IdIterable<ID>, ID>
        extends CustomCrudRepository<T, ID>, SoftDeleteRepository<T, ID>{

}
