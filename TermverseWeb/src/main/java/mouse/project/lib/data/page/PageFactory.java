package mouse.project.lib.data.page;

import java.util.List;

public interface PageFactory {
    <T> Page<T> pageOf(List<T> elements, PageDescription description);
    PageDescription pageDescription(Integer page, Integer size);
}
