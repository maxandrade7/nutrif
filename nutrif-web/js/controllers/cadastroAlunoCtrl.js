angular.module("NutrifApp").controller("alunoCtrl", function ($scope, cronogramaRefeicaoService, refeicaoService, diaService, cursoService) {
   
   $scope.refeicoes = [];
   $scope.dias = [];
   $scope.cursos = []

   var carregarRefeicoes = function(){
   		refeicaoService.listarRefeicoes().success( function(data, status){
   			$scope.refeicoes = data;
   		}).error(function(data, status){
   			if(!data){
   				alert("Erro no refeições, tente novamente ou contate os administradores.");
   			}else{
   				alert(data.message);
   			}
   		});
   }

   var carregarDias = function(){
   		diaService.listarDias().success( function(data, status){
   			$scope.dias = data;
   		}).error(function(data, status){
   			if(!data){
   				alert("Erro ao carregar dias, tente novamente ou contate os administradores.");
   			}else{
   				alert(data.message);
   			}
   		});
   }

   var carregarCursos = function(){
   		cursoService.listarCursos().success( function(data, status){
   			$scope.cursos = data;
   		}).error(function(data, status){
   			if(!data){
   				alert("Erro ao carregar cursos, tente novamente ou contate os administradores.");
   			}else{
   				alert(data.message);
   			}
   		});
   }

   carregarCursos();
   carregarRefeicoes();
   carregarDias();
});