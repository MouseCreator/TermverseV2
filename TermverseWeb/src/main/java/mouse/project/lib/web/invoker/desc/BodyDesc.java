package mouse.project.lib.web.invoker.desc;

public interface BodyDesc extends ArgumentDesc {
    Class<?> expectedClass();
    boolean isCollection();
    boolean isFullBody();
    Class<?> collectionType();
    String attributeName();
}
