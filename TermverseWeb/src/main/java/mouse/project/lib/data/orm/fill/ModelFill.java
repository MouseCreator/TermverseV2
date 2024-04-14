package mouse.project.lib.data.orm.fill;

import mouse.project.lib.data.orm.desc.ModelDescription;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface ModelFill {
    <T> T createAndFill(ResultSet set, ModelDescription<T> description);
    <T> Optional<T> createAndOptional(ResultSet set, ModelDescription<T> description);
    <T> List<T> createList(ResultSet set, ModelDescription<T> description);
}
