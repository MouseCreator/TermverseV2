package mouse.project.lib.web.invoker.desc;

public interface AttributeDesc extends ArgumentDesc{
    Class<?> expectedType();
    String getAttribute();
}
