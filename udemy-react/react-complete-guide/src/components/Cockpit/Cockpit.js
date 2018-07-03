import React from 'react';
import cockpitClasses from './Cockpit.css';

const Cockpit = (props) => {
    const btnClass = props.showPersons ? cockpitClasses.Red : '';

    const classes=[];
    if (props.persons.length <= 2) {
      classes.push(cockpitClasses.Red);
    }
    if (props.persons.length <= 1) {
      classes.push(cockpitClasses.Bold);
    }

    return (
        <div className={ cockpitClasses.Cockpit }>
            <h1>hello there</h1>

            {/* this can be inefficient approach */}
            <p className={ classes.join(' ') }>blah blah blah</p>

            <button 
                className={ btnClass }
                onClick={ props.clicked }
            >
                Toggle Persons
            </button>   
        </div>   
    );
}

export default Cockpit;