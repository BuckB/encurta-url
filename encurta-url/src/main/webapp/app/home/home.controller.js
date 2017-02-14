(function() {
    'use strict';

    angular
        .module('encurtaUrlApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$stateParams', 'entity', 'Link', '$filter', '$window'];

    function HomeController ($scope, Principal, LoginService, $state, $stateParams, entity, Link, $filter, $window) {
        var vm = this;

        vm.link = entity;
        vm.save = save;
        vm.retrieve = retrieve;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.mostra = false;
        vm.mostraRetorno = false;
        vm.alias = null;
        vm.limit = 10;

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
            vm.tempo = result.time_taken + 'ms';
            vm.shortened = 'http://www.bemobi/' + result.alias;
            vm.original = result.url;
            vm.isSaving = false;
            vm.mostra = true;

        }

        function onSaveError () {
            vm.isSaving = false;
            vm.mostra = false;
        }

        function retrieve() {
            Link.query(
                function(result) {
                    vm.result = result;
                    vm.searchQuery = vm.alias;
                    vm.urlsResgatadas = $filter('filter')(vm.result, {alias: vm.alias});
                    if (vm.urlsResgatadas.length == 1){
                        $window.open(vm.urlsResgatadas[0].url, '_blank');
                    } else {
                        vm.mostraRetorno = true;
                    }
                }
            );
        }
    }
})();
