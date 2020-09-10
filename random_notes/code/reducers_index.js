import { combineReducers} from "redux";

import constantsReducer from "./constantsReducer";
import searchFormReducer from "./searchFormReducer";
import errorsReducer from "./errorsReducer";
import drilldownQueryReducer from "./drilldownQueryReducer";

export default combineReducers({
    constantsStore: constantsReducer,
    searchFormStore: searchFormReducer,
    errorStore: errorsReducer,
    drilldownQueryStore: drilldownQueryReducer
});
