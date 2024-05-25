package mouse.project.termverseweb.service.sort;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.model.UserStudySet;
import org.springframework.stereotype.Component;

import java.util.Comparator;
@Component
@Service
public class StudySetSorter {
    public static Comparator<UserStudySet> byName = Comparator.comparing(u -> u.getStudySet().getName());
    public static Comparator<UserStudySet> byCreationDateNew = Comparator.comparing(u -> u.getStudySet().getCreatedAt());
    public static Comparator<UserStudySet> byCreationDateOld = byCreationDateNew.reversed();
    public Comparator<UserStudySet> chooseComparator(String type) {
        return switch (type) {
            case "name" -> byName;
            case "recent" -> byCreationDateNew;
            case "oldest" -> byCreationDateOld;
            default -> (s1, s2) -> 0;
        };
    }
}
