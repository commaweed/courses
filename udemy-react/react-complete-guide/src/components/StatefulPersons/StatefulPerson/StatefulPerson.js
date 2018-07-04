import  React, { Component } from 'react';
import personClasses from './StatefulPerson.css';
// import withClassAsStatefulWrapper from '../../../hoc/withClassAsStatefulWrapper';
import withClassForwardedRef from '../../../hoc/withClassForwardedRef';
import Aux from '../../../hoc/Aux';
import PropTypes from 'prop-types';
import { AuthContext } from '../../../containers/App/App';

class StatefulPerson extends Component {

    constructor(props) {
        super(props); 
        console.log('[StatefulPerson.js] create constructor', props);
        this.inputElement = React.createRef(); // react 16.3
    }

    componentWillMount() {
        console.log('[StatefulPerson.js] create componentWillMount');
    }

    componentDidMount() {
        console.log('[StatefulPerson.js] create componentDidMount');

        // gives focus to the first element (if this is the first element)
        if (this.props.position === 0) {
            // this.inputElement.focus();  // prior to 16.3
            this.inputElement.current.focus(); // 16.3
        }
    }

    focus() {
        this.inputElement.current.focus();
    }

    // references are only available in stateful components
    // refs are mostly for controlling focus and media playback and not for styling or displaying
    render() {
        console.log('[StatefulPerson.js] render');
        return (
            <Aux>
                { this.props.authenticated ? <p>I'm passed-down authentic</p> : null }
                <AuthContext.Consumer>
                    { (globalAuth) => globalAuth ? <p>I'm globally authentic</p> : null }
                </AuthContext.Consumer>
                <p onClick={ this.props.click }>I'm { this.props.name } and I'm { this.props.age } years old!</p>
                <p>{ this.props.children}</p>
                <input 
                    // ref={ (inputArgs) => { this.inputElement = inputArgs } }
                    ref={ this.inputElement }
                    type="text" 
                    onChange={ this.props.changed } 
                    value={ this.props.name } 
                />
            </Aux>
        );
    }
}

StatefulPerson.propTypes = {
    click: PropTypes.func,
    name: PropTypes.string,
    age: PropTypes.number,
    changed: PropTypes.func
}

export default withClassForwardedRef(StatefulPerson, personClasses.Person);