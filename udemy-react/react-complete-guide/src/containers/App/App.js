import React, { Component } from 'react';
// import Persons from '../../components/Persons/Persons';
import StatefulPersons from '../../components/StatefulPersons/StatefulPersons';
import Cockpit from '../../components/Cockpit/Cockpit';
import appClasses from './App.css';
import Aux from '../../hoc/Aux';
import withClassAsWrapper from '../../hoc/withClassAsWrapper';

// 16.3 global context example (works with providers and consumers); creates a jsx component
// initial value of false (it can change); it will be provided to all children that it wraps
// export it so it can be imported in a child component that wants to use it
export const AuthContext = React.createContext(false);

class App extends Component {

  // constructor is not required
  constructor(props) {
    super(props); // this is required if using constructor
    console.log('[App.js] constructor', props);

    // older versions of react would initialize state as follows
    this.state = {
      persons: [
        { id: 1, name: 'Max', age: 28 },
        { id: 2, name: 'Manu', age: 28 },
        { id: 3, name: 'Test', age: 50 }
      ],
      otherState: 'some other value',
      showPersons: false,
      toggleClicked: 0,
      authenticated: false,
      globalAuth: false // keeping original to demonstrate both
    }
  }

  // 16.3 AVOID THIS ONE (because they can be used incorrectly) - use the new methods instead
  componentWillMount() {
    console.log('[App.js] componentWillMount');
  }

  // 16.3 AVOID THIS ONE (because they can be used incorrectly) - use the new methods instead
  componentWillUpdate(nextProps, nextState) {
      console.log('[App.js] internal update componentWillUpdate', nextProps, nextState); 
  }

    // 16.3 AVOID THIS ONE (because they can be used incorrectly) - use the new methods instead
  // componentWillReceiveProps() {}

  // 16.3 - executed whenever your props are updated - called before rendering / mounting
  // state should rarely be coupled to your props
  static getDerivedStateFromProps(nextProps, prevState) {
    console.log('[App.js] getDerivedStateFromProps', nextProps, prevState);

    return prevState; // typically return a new state by using the props
  }

  // 16.3 - executed right before the DOM does update
  // great place to save the current scrolling position of the component (and set it in componentDidUpdate())
  getSnapshotBeforeUpdate() {
    console.log('[App.js] getSnapshotBeforeUpdate');
    return null;
  }

  componentDidMount() {
    console.log('[App.js] componentDidMount');
  }

  shouldComponentUpdate(nextProps, nextState) {
    console.log('[App.js] internal update shouldComponentUpdate', nextProps, nextState); 
    return nextState.persons !== this.state.persons || nextState.showPersons !== this.state.showPersons;
    // return true; 
  }

  componentDidUpdate() {
      console.log('[App.js] internal update componentDidUpdate'); 
  }  

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

    // setState is an ansynchronous call, so we need to update toggleClicked in this fashion
    this.setState( (prevState, props) => {
      return {
        showPersons: !doesShow,
        toggleClicked: prevState.toggleClicked + 1
      }
    });
  }

  deletePersonHandler = (personIndex) => {
    const persons = [...this.state.persons]; // or use the spread operator to copy
    persons.splice(personIndex, 1); // remove that element from the array
    this.setState({persons: persons});
  }

  loginHandler = () => {
    this.setState({ authenticated: true, globalAuth: true });
  }

  render() {
    console.log('[App.js] Inside render');

    let persons = null;
    if (this.state.showPersons) {
      persons = (    
        <StatefulPersons 
          persons={ this.state.persons } 
          clicked={ this.deletePersonHandler } 
          changed={ this.nameChangeHandler } 
          isAuthenticated={ this.state.authenticated }
        />
      );
    }

    return (
      <Aux>   
          <button onClick={ () => this.setState({showPersons: true}) }>Show Persons</button> 
          <Cockpit 
            showPersons={ this.state.showPersons }
            persons={ this.state.persons }
            clicked={ this.togglePersonsHandler }
            login={ this.loginHandler }
          />
          <AuthContext.Provider value={ this.state.globalAuth }>
            { persons }
          </AuthContext.Provider>
      </Aux>
    );
  }
}

export default withClassAsWrapper(App, appClasses.App);
