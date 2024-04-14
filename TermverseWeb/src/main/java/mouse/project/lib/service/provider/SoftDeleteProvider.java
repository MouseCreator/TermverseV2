package mouse.project.lib.service.provider;

import mouse.project.lib.service.helper.SoftDeleteHelper;
import mouse.project.lib.service.repository.SoftDeleteRepository;

public interface SoftDeleteProvider extends ServiceProvider {
    <MODEL, ID> SoftDeleteHelper<MODEL, ID> with(SoftDeleteRepository<MODEL, ID> model);
}
