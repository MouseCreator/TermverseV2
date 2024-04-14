package mouse.project.lib.tests.build;

import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.injector.ScanUtils;
import mouse.project.lib.tests.annotation.InitBeforeEach;

import java.lang.reflect.Field;
import java.util.List;
@Service
public class TestClassInitializer {
    public void scanAndInitialize(Object testObj, Inj inj) {
        ScanUtils scanUtils = ScanUtils.getInstance();
        Class<?> aClass = testObj.getClass();
        List<Field> annotatedFields = scanUtils.getAnnotatedFields(aClass, InitBeforeEach.class);
        for (Field field : annotatedFields) {
            Class<?> type = field.getType();
            Object obj = inj.get(type);
            field.setAccessible(true);
            try {
                field.set(testObj, obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot set field " + field + " for test class", e);
            }
        }
    }
}
