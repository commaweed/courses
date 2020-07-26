public class RestTemplateQueryHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateQueryHandler.class);
    
    final RestTemplate restTemplate;
    
    public RestTemplateQueryHandler(RestTemplate restTemplate) {
        // validate
        this.restTemplate = restTemplate;
    }
    
    public static <T> HttpEntity<T> createJsonHttpRequestEntity(
        HttpMethod requestMethod,
        T postBody,
        String userDn
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        switch (requestMethod) {
            case POST:
            case PUT:
                headers.setContentType(MediaType.APPLICATION_JSON));
        }
        
        return createHttpRequestEntity(requestMethod, headers, postBody, userDn);
    }
    
    public static <T> HttpEntity<T> createHttpRequestEntity(
        HttpMethod requestMethod,
        HttpHeaders headers,
        T postBody,
        String userDn
    ) {
        //validate
        
        // user headers here
        
        HttpEntity<T> requestEntity = new HttpEntity<>(postBody, headers);
        return requestEntity;
    }
    
    private <S extends RuntimeException> void handleQuerySubmissionException(
        Exception querySubmissionException,
        HttpMethod requestMethod,
        String url,
        Map<String, String> uriValueMappings,
        HttpEntity<?> requestEntity,
        Class<S> returnException
    ) {
        LOGGER.error(
            getExceptionMetadata(
                querySubmissionException, requestMethod, url, uriValueMappings, requestEntity
            ),
            querySubmissionException
        );
        
        String errorMessage = querySubmissionException.toString() + ": ";
        if (querySubmissionException instanceof HttpStatusCodeException) {
            String serverBody = StringUtils.trimToNull(
                ((HttpStatusCodeException) querySubmissionException).getResponseBodyAsString()
            );
            errorMessage += serverBody == null ? "NONE" : serverBody;
        }
        
        throw ExceptionUtil.instantiateNewException(returnException, errorMessage);
        );
    }
    
    public <T, S extends RuntimeException> ResponseEntity<T> submitQuery(
        HttpMethod requestMethod,
        String url,
        Map<String, String> uriValueMappings,
        HttpEntity<?> requestEntity,
        ParameterizedTypeReference<T> responseType,
        Class<S> exceptionType
    ) {
        //validate
        
        ResponseEntity<T> serverResults = null;
        try {
            if (uriValueMappings != null) {
                serverResults = this.restTemplate.exchange(url, requestMethod, requestEntity, responseType, uriValueMappings);
            } else {
                serverResults = this.restTemplate.exchange(url, requestMethod, requestEntity, responseType);
            }
        } catch (Exception e) {
            handleQuerySubmissionException(e, requestMethod, url, uriValueMappings, requestEntity, exceptionType);
        }
        
        return serverResults;
    }
    
    public <T, S extends RuntimeException> ResponseEntity<T> submitQuery(
        HttpMethod requestMethod,
        String url,
        Map<String, String> uriValueMappings,
        HttpEntity<?> requestEntity,
        Class<T> responseType,
        Class<S> exceptionType
    ) {
        //validate
        
        ResponseEntity<T> serverResults = null;
        try {
            if (uriValueMappings != null) {
                serverResults = this.restTemplate.exchange(url, requestMethod, requestEntity, responseType, uriValueMappings);
            } else {
                serverResults = this.restTemplate.exchange(url, requestMethod, requestEntity, responseType);
            }
        } catch (Exception e) {
            handleQuerySubmissionException(e, requestMethod, url, uriValueMappings, requestEntity, exceptionType);
        }
        
        return serverResults;
    }
    
    private static String getExceptionMetadata(
        Exception exception,
        HttpMethod requestMethod,
        String url,
        Map<String, String> uriValueMappings,
        HttpEntity<?> requestEntity    
    ) {
        StringBuilder errorMessage = new StringBuilder();

        errorMessage.append(exception.getClass().getSimpleName() + " occurred while retrieving data for: ");
        
        // append url stuff
        errorMessage.append("\nURL: " + url);
        if (uriMappings != null) {
            errorMessage.append("\nMap Values: " + uriValueMappings);
        }
        
        // append request stuff
        errorMessage.append("\nRequest Method: " + requestMethod);
        errorMessage.append("\nHeaders: " + requestEntity.getHeaders());
        switch (requestMethod) {
            case POST:
            case PUT:
                errorMessage.append("\nPost Body: " + requestEntity.getBody().toString());
                break;
        }
        
        // append the actual error
        if (exception instanceof HttpStatusCodeException) {
            HttpStatusCodeException statusCodeException = (HttpStatusCodeException) exception;
            errorMessage.append("\nHTTP Status Code: " + statusCodeException.getStatusCode());
            errorMessage.append("\nReponse Body: " + statusCodeException.getResponseBodyAsString());
        } else {
            errorMessage.append("\nException: " + exception.toString());
        }
        
        errorMessage.append("\n");
        
        return errorMessage.toString();
    }
}

ExceptionUtil.java

public class ExceptionUtil {
    public static <S> S instantiateNewException(Class<S> exceptionType) {
        return instantiateNewException(exceptionType, null);
    }
    
    public static <S> S instantiateNewException(Class<S> exceptionType, String error) {
        S newException = null;
        
        try {
            if (error != null) {
                if (StringUtils.trimToNull(error) == null) {
                    error = "No Error Message";
                }
            
                newException = exceptionType.getConstructor(String.class).newInstance(error);
            } else {
                newException = exceptionTYpe.getConstructor().newInstance();
            }
        } catch (
            InstantiationException |
            IllegalAccessException |
            IllegalArgumentException |
            InvocationTargetException |
            NoSuchMethodException |
            SecurityException e1
        ) {
            throw new RuntimeException("FATAL: Unable to construct exception type for [" + exceptionType + "]");
        }
        
        return newException;
    }
    
}
