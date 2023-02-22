'use strict';
angular.module('todo')
    .factory('service', ['$http', function ($http) {
        return {
            getItems: function () {
                return $http.get('api/todo');
            },
            postItem: function (item) {
                return $http.post('api/todo', item);
            },
            putItem: function (item) {
                return $http.put('api/todo', item);
            },
            deleteItem: function (id) {
                return $http({
                    method: 'DELETE',
                    url: 'api/todo/' + id
                });
            }
        };
    }]);
