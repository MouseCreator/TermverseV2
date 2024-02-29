package mouse.project.termverseweb.lib.service.helper;

import mouse.project.termverseweb.lib.service.repository.SoftDeleteRepository;

public interface SoftDeleteProvider extends ServiceProvider {
    <MODEL, ID> SoftDeleteHelper<MODEL, ID> with(SoftDeleteRepository<MODEL, ID> model);
}
