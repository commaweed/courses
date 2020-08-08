/**
 * Represents a factory to demonstrate fetching data from a local file.  Angular
 * has factory, service, and provider; each differ in how they are created.
 * factory - standard javascript factory
 * service - create with new function().  But it is a singleton.
 * provider - special $get
 */
(function () {
  'use strict';

  // get a handle to the ng-app
  var tjApp = angular.module('tjApp');

  // add a factory
  var tjAppFactory = tjApp.factory('tjFactory', [
    '$http',
    function($http) {
      return {

        // return the promise that will be the result of fetching the json from the local file
        // we want to makesure we return the promise and not implement callbacks here so that
        // the scope values can be updated in the success callback
        getResults: function() {
          return $http({
            method: "GET",
            url: "results.json",
            cache: true
          });

        }
      };
    }
  ]);

})();
