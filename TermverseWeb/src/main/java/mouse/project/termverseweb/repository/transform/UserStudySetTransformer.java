package mouse.project.termverseweb.repository.transform;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.model.UserStudySetModel;
@Service
public class UserStudySetTransformer {

    public UserStudySet transform(UserStudySetModel model) {
        UserStudySet userStudySet = new UserStudySet();
        User user = new User();
        user.setId(model.getUserId());
        user.setName(model.getUserName());
        user.setProfilePictureUrl(model.getUserPictureUrl());
        userStudySet.setUser(user);

        StudySet studySet = new StudySet();
        studySet.setId(model.getSetId());
        studySet.setName(model.getSetName());
        studySet.setPictureUrl(model.getSetPictureUrl());
        studySet.setCreatedAt(model.getSetCreatedAt());

        userStudySet.setStudySet(studySet);

        userStudySet.setType(model.getType());
        return userStudySet;
    }
}
