package mouse.project.termverseweb.error;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.error.ErrorStyle;
import mouse.project.lib.web.response.ErrorResponse;

@Service
public class ErrorStyleImpl implements ErrorStyle {

    @Override
    public String styleError(ErrorResponse response) {
        return String.format("{ status: %d, error: \"%s\" }", response.getStatus(), response.getMessage());
    }
}
