package mouse.project.termverseweb.model;

import lombok.Data;
import mouse.project.lib.data.orm.annotation.FromTable;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;

@Data
@Model
public class SetTermModel {
    @FromTable("study_sets")
    private StudySet studySet;
    @FromTable("terms")
    private Term term;
}
