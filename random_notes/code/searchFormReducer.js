import { clone } from 'ramda';
import { hashHistory } from 'react-router';

export default function reducer(
    state = {
        queryResults: {},
        queryRequestParams: {},
        queryString: null,
        fetching: false,
        fetched: false,
        error: null
    },
    action
) {
    switch (action.type) {
        case "SUBMIT_QUERY": {
            const queryUri = createQueryStringFromQueryParams(action.payload);
            
            if (queryUri) {
                hashHistory.replace(queryUri);
            }
            
            return {
                ...state,
                fetching: true,
                queryRequestParams: action.payload,
                queryString: queryUri
            };
        }
        
        case "SUBMIT_QUERY_REJECTED": {
            return {
                ...state,
                fetching: false,
                queryResults: { error: action.payload }
            };         
        }
        
        case "SUBMIT_QUERY_FULFILLED": {
            return {
                ...state,
                fetching: false,
                fetched: true,
                queryResults: action.payload
            };          
        }
    }
    
    return state;
}

const createQueryStringFromQueryParams = (queryRequestParams) => {
    let queryUri = null;
    
    if (queryRequestParams) {
        const queryParamsClone = clone(queryRequestParams);
        
        delete queryParamsClone['pageStart'];
        delete queryParamsClone['pageLength'];
        
        queryUri = "?" + Object.keys(queryParamsClone.map(option => 
            `${option}=${encodeURIComponent(queryParamslone[option])}`
        ).join('&');
    }
    return queryUri;
};
