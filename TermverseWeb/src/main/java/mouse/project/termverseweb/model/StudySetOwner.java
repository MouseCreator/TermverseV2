package mouse.project.termverseweb.model;

import lombok.Data;

@Data
public class StudySetOwner {
    private StudySet studySet;
    private User owner;

    public StudySetOwner(StudySet studySet, User owner) {
        this.studySet = studySet;
        this.owner = owner;
    }
}
