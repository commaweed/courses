import  React, { Component } from 'react';
import personClasses from './StatefulPerson.css';

class StatefulPerson extends Component {

    constructor(props) {
        super(props); 
        console.log('[StatefulPerson.js] create constructor', props);
    }

    componentWillMount() {
        console.log('[StatefulPerson.js] create componentWillMount');
    }

    componentDidMount() {
        console.log('[StatefulPerson.js] create componentDidMount');
    }

    render() {
        console.log('[StatefulPerson.js] render');
        return (
            <div className={ personClasses.Person }>
                <p onClick={ this.props.click }>I'm { this.props.name } and I'm { this.props.age } years old!</p>
                <p>{ this.props.children}</p>
                <input type="text" onChange={ this.props.changed } value={ this.props.name } />
            </div>
        );
    }
}

export default StatefulPerson;