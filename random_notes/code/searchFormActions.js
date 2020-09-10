import axios from "axios";

export function submitQuery(requestParams) {
    console.log("submitting requestParams", requestParams);
    
    return function(dispatch) {
        // initiate the query (puts state into fetching mode for load mask)
        dispatch({ type: "SUBMIT_QUERY", payload: requestParams });
        
        // submit the query to the server
        axios.get('blah', {
            params: requestParams,
            headers: { "ACCEPT:": "application/json" }
        })
        .then((response) => {
            console.log("queryResults", response.data);
            dispatch({ type: "SUBMIT_QUERY_FULFILLED", payload: response.data });
        })
        .catch((error) => {
            dispatch({ type: "ERROR_OCCURRED", payload: error });
            dispatch({ type: "SUBMIT_QUERY_RECEIVED", payload: error.response });
        });
    }
}
