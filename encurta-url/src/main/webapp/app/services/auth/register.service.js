(function () {
    'use strict';

    angular
        .module('encurtaUrlApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
