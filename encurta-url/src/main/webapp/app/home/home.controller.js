(function() {
    'use strict';

    angular
        .module('encurtaUrlApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$stateParams', 'entity', 'Link'];

    function HomeController ($scope, Principal, LoginService, $state, $stateParams, entity, Link) {
        var vm = this;

        vm.link = entity;
        vm.save = save;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.mostra = false;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function save () {
            vm.isSaving = true;
            Link.save(vm.link, onSaveSuccess, onSaveError);
            }

        function onSaveSuccess (result) {
            $scope.$emit('encurtaUrlApp:linkUpdate', result);
            console.log('result > ',result);
            vm.tempo = result.time_taken;
            vm.shortened = result.shortened;
            vm.original = result.original;
            vm.isSaving = false;
            vm.mostra = true;

        }

        function onSaveError () {
            vm.isSaving = false;
            vm.mostra = false;
        }
    }
})();
