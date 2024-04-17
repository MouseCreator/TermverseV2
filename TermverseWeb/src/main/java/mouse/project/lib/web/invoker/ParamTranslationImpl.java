package mouse.project.lib.web.invoker;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.utils.Utils;
import mouse.project.lib.web.exception.TranslationException;

import java.util.HashMap;
import java.util.Map;
@Service
public class ParamTranslationImpl implements ParamTranslation {
    @Override
    public Object translate(String from, Class<?> type) {
        Class<?> target = Utils.fromPrimitive(type);
        Translation translation = preparedMap.get(target);
        if (translation != null) {
            return translation.map(from);
        }
        throw new TranslationException("Cannot translate string to parameter. Unexpected type: " + type);
    }

    private final Map<Class<?>, Translation> preparedMap;

    @Auto
    public ParamTranslationImpl() {
        preparedMap = new HashMap<>();
        prepare(preparedMap);
    }

    private void prepare(Map<Class<?>, Translation> preparedMap) {
        preparedMap.put(Integer.class, Integer::parseInt);
        preparedMap.put(Float.class, Float::parseFloat);
        preparedMap.put(Long.class, Long::parseLong);
        preparedMap.put(Double.class, Double::parseDouble);
        preparedMap.put(String.class, s->s);
        preparedMap.put(Short.class, Short::parseShort);
        preparedMap.put(Byte.class, Byte::parseByte);
        preparedMap.put(Object.class, s->s);
    }
    @FunctionalInterface
    private interface Translation {
        Object map(String s);
    }

}
