import  React from 'react';
import personClasses from './Person.css';

const person = (props) => {

    // if (Math.random() >= 0.7) {
    //     throw new Error("we got an error");
    // }

    return (
        <div className={personClasses.Person}>
            <p onClick={props.click}>I'm {props.name} and I'm {props.age} years old!</p>
            <p>{props.children}</p>
            <input type="text" onChange={props.changed} value={props.name} />
        </div>
    );
};

export default person;