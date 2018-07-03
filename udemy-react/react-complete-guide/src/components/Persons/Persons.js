import  React from 'react';

import Person from './Person/Person';

const persons = (props) => props.persons.map( 
    (person, index) => 
        <Person 
            click={() => props.clicked(index) }
            name={ person.name } 
            age={ person.age }

            // assign a key property so react will only render the element that changes and not the entire list
            key={ person.id }

            changed={ (event) => props.changed(event, person.id) }
        />   
);

export default persons;