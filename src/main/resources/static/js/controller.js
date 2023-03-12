'use strict';
angular.module('todo')
    .controller('controller', ['$scope', '$location', 'service', function ($scope, $location, service) {
        $scope.error = '';
        $scope.loadingMessage = '';
        $scope.todoList = null;
        $scope.editingInProgress = false;
        $scope.new = '';

        $scope.editTodo = {
            content: '',
            id: 0,
            done: false
        };

        $scope.done = function (todo) {
            service.putItem(todo).error(function (err) {
                todo.done = !todo.done;
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.edit = function (todo) {
            todo.edit = !todo.edit;
            if (todo.edit) {
                $scope.editTodo.content = todo.content;
                $scope.editTodo.id = todo.id;
                $scope.editTodo.done = todo.done;
                $scope.editingInProgress = true;
            } else {
                $scope.editingInProgress = false;
            }
        };

        $scope.populate = function () {
            service.getItems().success(function (results) {
                $scope.todoList = results;
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.delete = function (id) {
            service.deleteItem(id).success(function (results) {
                $scope.populate();
                $scope.loadingMessage = results;
                $scope.error = '';
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.update = function (todo) {
            service.putItem($scope.editTodo).success(function (results) {
                $scope.populate();
                $scope.editSwitch(todo);
                $scope.loadingMessage = results;
                $scope.error = '';
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.add = function () {
            function getOwner() {
                var owner = localStorage.getItem('owner') || 'unknown';
                localStorage.setItem('owner', owner);
                return owner;
            }

            service.postItem({
                'content': $scope.new,
                'owner': getOwner(),
                'done': 'false'
            }).success(function (results) {
                $scope.newTodoCaption = '';
                $scope.populate();
                $scope.loadingMessage = results;
                $scope.error = '';
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMsg = '';
            })
        };
    }]);
