package mouse.project.termverseweb.filters.argument;

import lombok.Getter;

@Getter
public class OptionalAuthorization {
    private Long userDatabaseId;
    private String username;

    private boolean empty;

    private OptionalAuthorization() {
    }
    public static OptionalAuthorization of(Long id, String name) {
        OptionalAuthorization optionalAuthorization = new OptionalAuthorization();
        optionalAuthorization.empty = false;
        optionalAuthorization.userDatabaseId = id;
        optionalAuthorization.username = name;
        return optionalAuthorization;
    }
    public static OptionalAuthorization empty() {
        OptionalAuthorization optionalAuthorization = new OptionalAuthorization();
        optionalAuthorization.empty = true;
        return optionalAuthorization;
    }

    public boolean isPresent() {
        return !empty;
    }
}
