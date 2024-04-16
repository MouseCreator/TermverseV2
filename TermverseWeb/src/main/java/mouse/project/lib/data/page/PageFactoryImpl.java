package mouse.project.lib.data.page;

import mouse.project.lib.ioc.annotation.Service;

import java.util.List;
@Service
public class PageFactoryImpl implements PageFactory {
    @Override
    public <T> Page<T> pageOf(List<T> elements, PageDescription description) {
        return new PageImpl<>(elements, description);
    }

    @Override
    public PageDescription pageDescription(Integer page, Integer size) {
        return new PageDescriptionImpl(page, size);
    }
}
