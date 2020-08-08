/**
 * Represents the controller for my simple angular solution.  It will fetch data from
 * the factory. 
 */
(function () {
  'use strict';

  // get a handle to the app module
  var tjApp = angular.module('tjApp');

  // add a controller to it
  var tjAppController = tjApp.controller('tjController', [
    '$scope',     // inject the scope
    'tjFactory',  // inject the factory
    function($scope, tjFactory) {

      // The factory.getResults() returns a promise - set scope data once it returns
      // it is asynchronous so we need to ensure the scope variable is in the callback
      tjFactory.getResults().success(function(response) {
        $scope.results = response;  
      });

    }
  ]);

})();
