export default function reducer(
    state = {
        constants: [],
        fetching: false,
        fetched: false,
        error: null
    }, 
    action
) {
    switch (action.type) {
        case "FETCH_CONSTANTS": {
            return { ...state, fetching: true };
        }
        
        case "FETCH_CONSTANTS_REJECTED": {
            return { ...state, fetching: false, error: action.payload };
        }
        
        case "FETCH_CONSTANTS_FULFILLED": {
            return { ...state, fetching: true, constants: action.payload };
        }        
    }
    return state;
}
