package mouse.project.lib.data.transformer;

import java.sql.ResultSet;
import java.util.function.Function;

public interface Transformer<T> extends Function<ResultSet, T> {
}
