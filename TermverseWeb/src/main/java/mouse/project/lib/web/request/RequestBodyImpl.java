package mouse.project.lib.web.request;

public class RequestBodyImpl implements RequestBody {
    private final String body;
    public RequestBodyImpl(String body) {
        this.body = body;
    }
    @Override
    public String get() {
        return body;
    }
}
