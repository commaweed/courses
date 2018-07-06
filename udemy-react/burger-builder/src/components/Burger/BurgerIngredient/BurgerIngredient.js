import React, { Component } from 'react';

import classes from './BurgerIngredient.css';

import PropTypes from 'prop-types';

const ingredientFormat = (className, ...children) => (
    <div className={classes[className]}>{ children }</div>
);

class BurgerIngredient extends Component {

    render() {
        let ingredient = null;

        switch (this.props.type) {
            case 'bread-bottom':
                ingredient = ingredientFormat('BreadBottom');
                break;
            case 'bread-top':
                ingredient = ingredientFormat(
                    'BreadTop',
                    [
                        ingredientFormat('Seeds1'),
                        ingredientFormat('Seeds2')
                    ]
                );
                break;
            case 'cheese':
                ingredient = ingredientFormat('Cheese');
                break;                
            case 'meat':
                ingredient = ingredientFormat('Meat');
                break;
            case 'salad':
                ingredient = ingredientFormat('Salad');
                break;   
            case 'bacon':
                ingredient = ingredientFormat('Bacon');
                break;                      
            default:
                throw new Error("Invalid ingredient type supplied " + this.props.type);
        }

        return ingredient;
    }
}

BurgerIngredient.PropTypes = {
    type: PropTypes.string.isRequired
};

export default BurgerIngredient;