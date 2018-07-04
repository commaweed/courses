import React from 'react';
import cockpitClasses from './Cockpit.css';
import Aux from '../../hoc/Aux';

const Cockpit = (props) => {
    const btnClass = [ cockpitClasses.Button ];
    if (props.showPersons) {
        btnClass.push(cockpitClasses.Red);
    }

    const classes=[];
    if (props.persons.length <= 2) {
      classes.push(cockpitClasses.Red);
    }
    if (props.persons.length <= 1) {
      classes.push(cockpitClasses.Bold);
    }

    return (
        <Aux>
            <h1>hello there</h1>

            {/* this can be inefficient approach */}
            <p className={ classes.join(' ') }>blah blah blah</p>

            <button 
                className={ btnClass.join(' ') }
                onClick={ props.clicked }
            >
                Toggle Persons
            </button>   

            <button onClick={ props.login }>Log In</button>
        </Aux>   
    );
}

export default Cockpit;