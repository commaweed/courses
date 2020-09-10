import store from '../app-redux/store';

const getRecord = (key) => {
    let matchingRecord  = null;
    const storeState = store.getState();
    if (storeState && storeState.constantsStore) {
        const constants = storeState.constantsStore.constants;
        if (constants && constants.length > 0) {
           matchingRecord = find(propEq('key', key))(constants); 
        }
    }
    
    return matchingRecord;
};
