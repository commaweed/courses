import { isEmpty } from 'ramda';

import { convertRequestParamsToFormFieldValues } from '../../views/SearchWidget/components/SearchForm/formFields';

/**
* Action that indicates a drilldown query was made.
*/
export function receiveDrilldownQuery(query, search) {
    return function(dispatch) {
        if (query && !isEmpty(query)) {
            dispatch({ type: "DRILLDOWN_QUERY_RECEIVED", payload: createDrilldownQueryRequestParams(query, search) });
        }
    }
}

function createDrilldownQueryRequestParams(query, search) {
    let drilldownQuery = null;
    const fieldValues = convertRequestParamsToFormFieldValues(query);
    if (fieldValues) {
        drilldownQuery = {
            query: query,
            search: search,
            fieldMetadata: fieldValues
        }
    }
    return drilldownQuery;
}

/**
* Action that indicates the drilldown query has been submitted to the server.  This is needed to prevent the query from
* remaining resident for the child components.
*/
export function fulFillDrilldownQuery() {
    return function(dispatch) {
        dispatch({ type: "DRILLDOWN_QUERY_FULFILLED", payload: null });
    }
}
