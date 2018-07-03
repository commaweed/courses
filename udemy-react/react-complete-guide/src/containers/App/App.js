import React, { Component } from 'react';
import Persons from '../../components/Persons/Persons';
import Cockpit from '../../components/Cockpit/Cockpit';
import appClasses from './App.css';

class App extends Component {
  state = {
    persons: [
      { id: 1, name: 'Max', age: 28 },
      { id: 2, name: 'Manu', age: 28 },
      { id: 3, name: 'Test', age: 50 }
    ],
    otherState: 'some other value',
    showPersons: false
  }

  // switchNameHandler = (newName) => {
  //   this.setState({persons:[
  //     { name: newName, age: 28},
  //     { name: 'Manu', age: 28},
  //     { name: 'Test', age: 33}
  //   ]});
  // }

  nameChangeHandler = ( event, id) => {
    const personIndex = this.state.persons.findIndex(person => person.id === id);
    // const person = this.state.persons[personIndex];
    // better to make a copy 
    const person = {
      ...this.state.persons[personIndex]
    }
    // alternative
    // const person = Object.assign({}, this.state.persons[personIndex]);

    person.name = event.target.value;

    // make a copy of the original state and add our one element
    const persons = [...this.state.persons];
    persons[personIndex] = person;

    this.setState({persons: persons});
  }

  togglePersonsHandler = () => {
    const doesShow = this.state.showPersons;
    this.setState({showPersons: !doesShow });
  }

  deletePersonHandler = (personIndex) => {
    const persons = [...this.state.persons]; // or use the spread operator to copy
    persons.splice(personIndex, 1); // remove that element from the array
    this.setState({persons: persons});
  }

  render() {
    let persons = null;
    if (this.state.showPersons) {
        persons = (    
          <Persons 
            persons={ this.state.persons } 
            clicked={ this.deletePersonHandler } 
            changed={ this.nameChangeHandler } 
          />
        );
    }

    return (
      <div className={appClasses.App}>      
        <Cockpit 
          showPersons={ this.state.showPersons }
          persons={ this.state.persons }
          clicked={ this.togglePersonsHandler }
        />
        { persons }
      </div>
    );
  }
}

export default App;
