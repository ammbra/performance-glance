'use strict';
angular.module('todo', ['ngRoute'])
    .config(['$routeProvider',  function ($routeProvider) {
        $routeProvider.when('/todo', {
            controller: 'controller',
            templateUrl: 'html/todo.html',
        }).otherwise({redirectTo: '/todo'});
    }]);
