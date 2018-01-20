/**
 * Interceptador para Requisições (Request) HTTP: verificação da autorização do usuário.
 */
nutrIFApp.config(['$httpProvider', function ($httpProvider) {
    // Adicionar interceptadores:
    $httpProvider.interceptors.push(function ($q, $injector, userService) {
        return {
            
            request: function (config) {                
                var user = userService.getUser();

                if (user) {
                    config.headers.Authorization = 'Basic ' + user;
                } else {
                    config.headers.authorization = '';
                }
                
                return config;
            }
        };
    });
}]);