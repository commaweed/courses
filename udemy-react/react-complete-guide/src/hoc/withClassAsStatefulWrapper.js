import React, { Component } from 'react';

// can be used to maintain state, or reach out to server, etc.
const withClassAsStatefulWrapper = (WrappedComponent, className) => {
    // return an anonymous class (class factory)
    return class extends Component {
       render() {
           return (
            <div className={ className }>
                <WrappedComponent 
                    ref={ this.props.forwardedRef }
                    { ...this.props } 
                />
            </div>
        );
       } 
    } 
}

export default withClassAsStatefulWrapper;