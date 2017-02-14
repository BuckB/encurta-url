(function() {
    'use strict';

    angular
        .module('encurtaUrlApp')
        .controller('LinkDetailController', LinkDetailController);

    LinkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Link'];

    function LinkDetailController($scope, $rootScope, $stateParams, previousState, entity, Link) {
        var vm = this;

        vm.link = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('encurtaUrlApp:linkUpdate', function(event, result) {
            vm.link = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
