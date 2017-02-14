(function() {
    'use strict';

    angular
        .module('encurtaUrlApp')
        .controller('LinkController', LinkController);

    LinkController.$inject = ['$scope', '$state', 'Link'];

    function LinkController ($scope, $state, Link) {
        var vm = this;

        vm.links = [];

        loadAll();

        function loadAll() {
            Link.query(function(result) {
                vm.links = result;
                vm.searchQuery = null;
            });
        }
    }
})();
