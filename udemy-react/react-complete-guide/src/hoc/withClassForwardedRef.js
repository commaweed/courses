import React, { Component } from 'react';

// uses 16.3 way for forwarding refs
const withClassForwardedRef = (WrappedComponent, className) => {
    // return an anonymous class (class factory)
    const WithClassForwardedRef = class extends Component {
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

    return React.forwardRef( 
        (props, ref) => {
            return <WithClassForwardedRef { ...props } forwardedRef={ref} />
        }
    );
}

export default withClassForwardedRef;