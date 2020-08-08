(function () {
    var person = angular.module("person");
    person.factory("PersonService", [
        '$http',
        'httpRestValue',
        function ($http, httpRestValue) {
            var PersonService = {
                data: {
                    currentPerson: {},
                    persons : []
                },
                getPerson: function (id) {
                    return $http.get(httpRestValue + "person/"+id)
                        .success(function success(data) {
                            PersonService.data.currentPerson = data;
                        })
                        .error(function error() {
                        });
                },
                getPersons : function(){
                    return $http.get(httpRestValue + "person/list")
                        .success(function success(data) {
                            PersonService.data.persons = data;
                        })
                        .error(function error() {
                        });
                },
                savePerson : function(person){
                    return $http.post(httpRestValue + "person/",person)
                        .success(function success() {
                            PersonService.getPersons();
                        })
                        .error(function error() {
                        });
                },
                deletePerson : function(id){
                    return $http.delete(httpRestValue + "person/"+id)
                        .success(function success() {
                            PersonService.getPersons();
                        })
                        .error(function error() {
                        });
                }
        };
        return PersonService;
    }
]);
})();

(function () {
    var person = angular.module("person");
    person.controller("PersonController",[
        '$scope',
        'PersonService',
        function($scope,PersonService){
            $scope.personData = PersonService.data;
        }
    ]);
})();
