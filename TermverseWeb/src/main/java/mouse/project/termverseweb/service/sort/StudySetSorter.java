package mouse.project.termverseweb.service.sort;

import mouse.project.lib.data.sort.SortOrder;
import mouse.project.lib.data.sort.SortOrderImpl;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.model.UserStudySet;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
public class StudySetSorter {

    private StudySetSorter() {
    }

    public static Comparator<UserStudySet> byName = Comparator.comparing(u -> u.getStudySet().getName());
    public static Comparator<UserStudySet> byCreationDateNew = Comparator.comparing(u -> u.getStudySet().getCreatedAt());
    public static Comparator<UserStudySet> byCreationDateOld = byCreationDateNew.reversed();
    public static SortOrder<UserStudySet> chooseSortOrder(String type) {
        Comparator<UserStudySet> userStudySetComparator = chooseComparator(type);
        return new SortOrderImpl<>(userStudySetComparator);
    }
    public static Comparator<UserStudySet> chooseComparator(String type) {
        return switch (type) {
            case "name" -> byName;
            case "recent" -> byCreationDateNew;
            case "oldest" -> byCreationDateOld;
            default -> (s1, s2) -> 0;
        };
    }

    public static Sort sortBy(String type) {
        return switch (type) {
            case "name" -> Sort.by(Sort.Direction.ASC, "name");
            case "recent" -> Sort.by(Sort.Direction.ASC, "createdAt");
            case "oldest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.unsorted();
        };
    }
}
