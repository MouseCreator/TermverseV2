package mouse.project.termverseweb.lib.service.repository;


public interface SoftDeleteCrudRepository<T, ID> extends CustomCrudRepository<T, ID>, SoftDeleteRepository<T, ID>{

}
