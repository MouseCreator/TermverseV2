package mouse.project.termverseweb.filters.argument;

import lombok.Getter;


@Getter
public class OptionalAuthentication {
    private String securityId;

    private boolean empty;

    private OptionalAuthentication() {
    }
    public static OptionalAuthentication of(String securityId) {
        OptionalAuthentication optionalAuthentication = new OptionalAuthentication();
        optionalAuthentication.empty = false;
        optionalAuthentication.securityId = securityId;
        return optionalAuthentication;
    }
    public static OptionalAuthentication empty() {
        OptionalAuthentication optionalAuthentication = new OptionalAuthentication();
        optionalAuthentication.empty = true;
        return optionalAuthentication;
    }

    public boolean isPresent() {
        return !empty;
    }


}
