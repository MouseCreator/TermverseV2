package mouse.project.lib.web.parse;

import java.util.Collection;

public interface BodyParser {
    <T> T parse(String body, Class<T> expectedType);
    <T> Collection<T> parseAll(String body, Class<T> expectedType);
    <T> T parseAttribute(String body, String attributeName, Class<T> expectedType);
    <T> Collection<T> parseAllByAttribute(String str, String attr, Class<T> aClass);
    String unparse(Object object);
}
