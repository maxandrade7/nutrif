angular.module("NutrifApp").controller("loginPretensaoCtrl", function (userService, $state, alunoService, $mdToast, $scope) {

    this.fazerLogin = function (aluno) {
        alunoService.fazerLogin(aluno)
            .success(function (data, status) {
            	$scope.aluno = data;
            	
            	console.log(data);
            	
                userService.storeUser(data);
                if($scope.aluno.acesso == true){
                	$state.go("pretensao.listar-pretensao");
                }else{
                	 $state.transitionTo("pretensao.atualizar-dados-aluno",{matricula:$scope.aluno.matricula});
                }
            })

            .error(function (data, status) {

                delete $scope.matricula;
                var _message = "";

                if (!data) {
                    _message = "Aluno inexistente"
                } else {
                    _message = data.mensagem
                }

                $mdToast.show(
                    $mdToast.simple()
                    .textContent(_message)
                    .position('top right')
                    .action('OK')
                    .hideDelay(6000)
                );
            });
    };
});
