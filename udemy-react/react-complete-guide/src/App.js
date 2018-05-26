import React, { Component } from 'react';
import Person from './Person/Person';
import './App.css';

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
    // make a copy before deleting because arrays are references
    // const persons = this.state.persons.slice(); // copy the original array
    const persons = [...this.state.persons]; // or use the spread operator to copy
    persons.splice(personIndex, 1); // remove that element from the array
    this.setState({persons: persons});
  }

  render() {

    const style = {
      backgroundColor: 'white',
      font: 'inherit',
      border: '1px solid blue',
      padding: '8px',
      cursor: 'pointer'
    };

    let persons = null;
    if (this.state.showPersons) {
        persons = (
          <div>
            {this.state.persons.map((person, index) => 
              <Person 
                click={() => this.deletePersonHandler(index) }
                name={ person.name } 
                age={ person.age }

                // assign a key property so react will only render the element that changes and not the entire list
                key={ person.id }

                changed={ (event) => this.nameChangeHandler(event, person.id) }
              />
            )}
          </div>
        );
    }

    return (
      <div className="App">
        <h1>hello there</h1>
        {/* this can be inefficient approach */}
        <button 
          style={style}
          // onClick={(event) => this.switchNameHandler('alternative')}
          onClick={ this.togglePersonsHandler }
        >
          Toggle Persons
        </button>

        { persons }

      </div>
    );
  }
}

export default App;

/*
{ this.state.showPersons ?
  <div>
    <Person 
      name={this.state.persons[0].name} 
      age={this.state.persons[0].age} 
    />
    <Person 
      name={this.state.persons[1].name} 
      age={this.state.persons[1].age} 
      click={this.switchNameHandler.bind(this, 'test 2')}
      changed={this.nameChangeHandler} 
    />
    <Person 
      name={this.state.persons[2].name} 
      age={this.state.persons[2].age} 
    >
      ksjdflkj sdklfjlksdj dsf
    </Person>
  </div>
  : null // render nothing
}
*/
