@ControllerAdvice
public class MyGlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) 
    private ErrorResponse handleUnknownExceptions(Exception cause) {
        // log here
        return ErrorResponse.getExceptionResponse(cause);
    }

}
