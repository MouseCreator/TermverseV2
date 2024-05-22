package mouse.project.lib.web.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private String details;

    public ErrorResponse() {
        status = 500;
        message = "";
        details = "";
    }

    public static ErrorResponse def() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setMessage("Server error");
        errorResponse.setDetails("");
        return errorResponse;
    }
    public void set(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public void set(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }
    public ErrorResponse(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public void from(ErrorResponse errorResponse) {
        this.status = errorResponse.status;
        this.message = errorResponse.message;
        this.details = errorResponse.details;
    }
}
