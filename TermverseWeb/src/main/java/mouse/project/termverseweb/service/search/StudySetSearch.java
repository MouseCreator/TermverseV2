package mouse.project.termverseweb.service.search;

import jakarta.annotation.PostConstruct;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.dto.studyset.StudySetWithOwnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Component
public class StudySetSearch {
    private final List<SearchCategoryHandler> searchCategoryHandlers;
    private Map<String, SearchCategoryHandler> handlers;

    @Autowired
    @Auto
    public StudySetSearch(@Collect(SearchCategoryHandler.class) List<SearchCategoryHandler> searchCategoryHandlers) {
        this.searchCategoryHandlers = searchCategoryHandlers;
    }

    @PostConstruct
    @After
    public void initializeHandlers() {
        this.handlers = new HashMap<>();
        for (SearchCategoryHandler h : searchCategoryHandlers) {
            SearchCategoryHandler prev = handlers.put(h.forCategory(), h);
            if (prev != null) {
                throw new IllegalStateException("Multiple handlers defined for search category: " + h.forCategory());
            }
        }
    }

    public List<StudySetWithOwnerDTO> search(String category, String query, Long userId, PageDescription pageDescription) {
        SearchCategoryHandler searchCategoryHandler = handlers.get(category);
        if (searchCategoryHandler == null) {
            throw new NoSuchElementException("No handler defined for category: " + category);
        }
        return searchCategoryHandler.search(query, userId, pageDescription);
    }
}
