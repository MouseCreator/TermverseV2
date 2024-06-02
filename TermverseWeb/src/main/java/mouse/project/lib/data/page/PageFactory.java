package mouse.project.lib.data.page;

import mouse.project.lib.data.sort.SortOrder;

import java.util.List;

public interface PageFactory {
    <T> Page<T> pageOf(List<T> elements, PageDescription description);
    PageDescription pageDescription(Integer page, Integer size);
    <T> Page<T> pageOf(List<T> list, PageDescription pageDescription, SortOrder<T> sortOrder);
    <T> Page<T> applyPageDescription(List<T> list, PageDescription pageDescription, SortOrder<T> sortOrder);
    <T> Page<T> applyPageDescription(List<T> list, PageDescription pageDescription);
}
