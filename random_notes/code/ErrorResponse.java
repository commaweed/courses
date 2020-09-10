@JsonINclude(JsonInclude.Include.NON_ABSENT)
public class ErrorReponse {

    private String message;
    private String exceptionType;
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getExceptionType() { return exceptionType; }
    
    public static ErrorResponse getExceptionResponse(Exception cause) {
        // validate cause
        
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Server-Side Error: " + cause.toString());
        response.exceptionType = cause.getClass().getName();
        return response;
    }
    
    // toString here
    
}
