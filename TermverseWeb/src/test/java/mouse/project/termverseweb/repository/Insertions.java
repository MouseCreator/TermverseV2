package mouse.project.termverseweb.repository;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.lib.service.model.IdIterable;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import mouse.project.termverseweb.model.*;
import mouse.project.termverseweb.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class Insertions {

    private final Factories factories;

    @Auto
    public Insertions(Factories factories) {
        this.factories = factories;
    }

    public List<StudySet> generateStudySets(String base, int count) {
        StudySetFactory factory = factories.getFactory(StudySetFactory.class);
        List<StudySet> sets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StudySet studySet = factory.studySet(base + "-" + (i + 1));
            sets.add(studySet);
        }
        return sets;
    }

    public List<User> generateUsers(String base, int values) {
        UserFactory userFactory = factories.getFactory(UserFactory.class);
        return getNInstances(i -> userFactory.user(base + "-" + (i + 1)), values);
    }

    public <T extends IdIterable<ID>, ID> List<T> saveAll(CustomCrudRepository<T, ID> crud, List<T> models) {
        return models.stream().map(crud::save).toList();
    }

    private <T> List<T> getNInstances(Function<Integer, T> supplyFuc, int count) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            T t = supplyFuc.apply(i);
            result.add(t);
        }
        return result;
    }

    public List<Term> generateTerms(String termsBaseName, int values) {
        TermFactory termFactory = factories.getFactory(TermFactory.class);
        return getNInstances(i -> termFactory.term(termsBaseName + (i+1), i+1), values);
    }

    public List<Tag> generateTags(User owner, String name, int count) {
        TagFactory tagFactory = factories.getFactory(TagFactory.class);
        return getNInstances(i -> tagFactory.tag(owner, name+(i+1)), count);
    }

    public List<SetTerm> bindSetTerms(StudySetTermRepository repository, StudySet studySet, List<Term> savedTerms) {
        return savedTerms.stream().map(t -> repository.save(new SetTerm(studySet, t))).toList();
    }

}
