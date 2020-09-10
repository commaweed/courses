import axios from "axios";

export function fetchConstants() {
    return function(dispatch) {
        // initiate the query (puts state into fetching mode for load mask)
        dispatch({ type: "FETCH_CONSTANTS", payload: null });
        
        axios.get('constants/all')
            .then((response) => {
                dispatch({ type: "FETCH_CONSTANTS_FULFILLED", payload: response.data });
            })
            .catch((error) => {
                dispatch({ type: "ERROR_OCCURRED", payload: error });
                dispatch({ type: "FETCH_CONSTANTS_REJECTED", payload: error.response });
            });
    }
}
