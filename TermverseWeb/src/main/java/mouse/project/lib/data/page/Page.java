package mouse.project.lib.data.page;

import java.util.List;

public interface Page<T> {
    PageDescription description();
    int totalElements();
    boolean isEmpty();
    List<T> getElements();

}
