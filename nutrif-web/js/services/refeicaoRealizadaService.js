angular.module("NutrifApp").factory("refeicaoRealizadaService", function($http, config){

    var _path = config.baseUrl() + "/refeicaoRealizada";

	var _inserirRefeicao = function(refeicao){
		return $http.post(_path + "/inserir", refeicao)
	};

	return {
		inserirRefeicao: _inserirRefeicao
	};

});
