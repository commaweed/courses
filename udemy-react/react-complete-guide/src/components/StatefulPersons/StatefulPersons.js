import  React, { PureComponent } from 'react';

import StatefulPerson from './StatefulPerson/StatefulPerson';

class StatefulPersons extends PureComponent {

    constructor(props) {
        super(props); 
        console.log('[StatefulPersons.js] constructor', props);
    }

    componentWillMount() {
        console.log('[StatefulPersons.js] componentWillMount');
    }

    componentDidMount() {
        console.log('[StatefulPersons.js] componentDidMount');
    }

    componentWillReceiveProps(nextProps) {
        console.log('[StatefulPersons.js] update componentWillReceiveProps', nextProps);
    }

    // PROVIDED BY PURECOMPONENT (compares changes)
    // shouldComponentUpdate(nextProps, nextState) {
    //     console.log('[StatefulPersons.js] update shouldComponentUpdate', nextProps, nextState); 
    //     // return nextProps.persons !== this.props.persons; 
    //     return true;
    // }

    componentWillUpdate(nextProps, nextState) {
        console.log('[StatefulPersons.js] update componentWillUpdate', nextProps, nextState); 
    }

    componentDidUpdate() {
        console.log('[StatefulPersons.js] update componentDidUpdate'); 
    }

    render() {
        console.log('[StatefulPersons.js] render');
        const persons = this.props.persons.map( 
            (person, index) => 
                <StatefulPerson 
                    click={() => this.props.clicked(index) }
                    name={ person.name } 
                    age={ person.age }
        
                    // assign a key property so react will only render the element that changes and not the entire list
                    key={ person.id }
        
                    changed={ (event) => this.props.changed(event, person.id) }
                />   
        );

        return (
            <div>{ persons }</div>
        );
    }
}

export default StatefulPersons;