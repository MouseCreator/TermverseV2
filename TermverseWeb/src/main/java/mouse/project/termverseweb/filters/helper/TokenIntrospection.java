package mouse.project.termverseweb.filters.helper;

public interface TokenIntrospection {
    String decodeAndValidate(String token);
}
