(function(){
    'use strict';

    angular.module('encurtaUrlApp').directive('protocolPrefix', protocolPrefix);

    function protocolPrefix() {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, element, attrs, controller){
                //Se não houver protocolo no inicio do input do usuario, adiciona.
                function garanteProtocolo(value){
                    if(value && !/^(https?):\/\//i.test(value)
                        && 'http://'.indexOf(value)!== 0
                        && 'https://'.indexOf(value) !== 0) {
                        controller.$setViewValue('http://' + value);
                        controller.$render();
                        return 'http://' + value;
                    } else {
                        return value;
                    }
                }
                controller.$formatters.push(garanteProtocolo);
                controller.$parsers.splice(0, 0, garanteProtocolo);
            }
        }
    }
})();
