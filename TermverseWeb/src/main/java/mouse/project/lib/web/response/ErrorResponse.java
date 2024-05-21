package mouse.project.lib.web.response;

import lombok.Data;

@Data
public class ErrorResponse {
    int status;
    String message;
    public static ErrorResponse def() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setMessage("Server error");
        return errorResponse;
    }
}
