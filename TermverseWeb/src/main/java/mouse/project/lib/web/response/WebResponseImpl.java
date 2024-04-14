package mouse.project.lib.web.response;

public class WebResponseImpl implements WebResponse {
    private final Object result;
    private final int status;
    public WebResponseImpl(int status, Object object) {
        this.result = object;
        this.status = status;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public int status() {
        return status;
    }
}
