/**
 * Represents my home-grown directive (as an attribute - alternatively, I could
 * have used restrict: 'E' to make it an element instead).
 */
(function () {
  'use strict';

    // get a handle to the app
    var tjApp = angular.module('tjApp');

    tjApp.directive('tjTypeahead', function() {
      return {
        // only allow this directive to be an attribute
        restrict: 'A', 

        // the html that will be inserted into th DOM
        templateUrl: 'tj-typeahead-template.html', 

        // isolate the scope to this directive
        // Isolated scope does not prototypically inherit from the parent scope
        // names on the left are internal scope names; names on the right are parent scope names 
        // parent scope names are specified by the attribute names on the directive in the main html
        // (e.g.) <div tj-typeahead dog="{{ dogvalue }}"></div> ==> dogType: '@dog'
        // use internal scope names in the link function
        scope: {
          people: '=' // two-way data-bind with the people attribute (changes will be seen in parent)
        },

        /**
         * Standard Directive function used to manipuate the DOM. Link function happens
         * in the post-link phase (i.e after compile and pre-link - all children elements
         * would have finished those phases first)
         * @param {Object} scope Angular scope object.
         * @param {Element} element The JQLite-wrapped element that this directive matches.
         * @param {Object} attrsMap normalized attribute name:value pairs.
         */
        link: function(scope, element, attrsMap) {
          // eventuanlly I will retrofit this to be a type ahead combobox
          // The DOM manipulation code to display the results will go here and they 
          // will act on the template DOM (element can use jquery methods)
        }
      };
    });

})();

