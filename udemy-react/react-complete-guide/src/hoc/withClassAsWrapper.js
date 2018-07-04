import React from 'react';

// not a functional component, but returns a functional component
const withClassAsWrapper = (WrappedComponent, className) => {
    return (props) => (
        <div className={ className }>
            <WrappedComponent { ...props } />
        </div>
    );
}

export default withClassAsWrapper;