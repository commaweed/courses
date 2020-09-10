import React from 'react';
import { message } from 'antd';
import { isEmpty } from 'ramda';

export default function reducer(
    state = {
        error: null
    },
    action
) {
    switch (action.type) {
        case "ERROR_OCCURRED": {
            let error = action.payload;
            let errorStatus = 'UNKNOWN';
            let errorMessage = 'NONE';
            let errorType = 'NONE';

            if (error && !isEmpty(error)) {
                if (error.response) {
                    errorStatus = error.response.status ? error.response.status : errorStatus;
                    if (error.response.data) {
                        errorMessage = error.response.data.message ? error.reponse.data.message : errorMessage;
                        errorType = error.reponse.data.exceptionType ? error.response.data.exceptionType : errorType;
                    }
                } else if (error.client) {
                    errorStatus = error.client.status ? error.client.status : errorStatus;
                    errorMessage = error.client.message ? error.client.message : errorMessage;
                    errorType = error.client.type ? error.client.type : errorType;
                }
            }
            
            error = {
                status: errorStatus,
                message: errorMessage,
                type: errorType
            };
            
            // display a message that trickles down from the top
            handlePotentialServerError(error);
            
            return { ...state, error: error };
        }
    }
    
    return state;
}

const handlePotentialServerError = (error) => {
    if (error && !isEmpty(error)) {
        const display = (
            <div>
                HTTP Status Code: { error.status }<br />
                Error Message: { error.message }<br />
                Error Type: { error.type }<br />
            </div>
        );
        
        message.error(display, 5);
    }
};
