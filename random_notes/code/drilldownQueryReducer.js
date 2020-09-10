export default function reducer(
    state = {
        drilldownQuery: null,
        hasSubmitted: false
    },
    action
) {
    switch (action.type) {
        case "DRILLDOWN_QUERY_RECEIVED": {
            return { ...state, hasSubmitted: false, drillDownQuery: action.payload };
        }
        
        case "DRILLDOWN_QUERY_FULFILLED": {
            return { ...state, hasSubmitted: true, drillDownQuery: null};
        }        
    }
    return state;
}
