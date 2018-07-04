import React, { Component } from 'react';

// can be used to maintain state, or reach out to server, etc.
const withClassAsWrapper = (WrappedComponent, className) => {
    // return an anonymous class (class factory)
    return class extends Component {
       render() {
           return (
            <div className={ className }>
                <WrappedComponent { ...this.props } />
            </div>
        );
       } 
    } 
}

export default withClassAsWrapper;