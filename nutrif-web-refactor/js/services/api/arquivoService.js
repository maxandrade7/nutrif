angular.module("NutrifApp").factory("arquivoService", function ($http, config) {

    var _path = config.baseUrl() + "/arquivo";

    var _upload = function (file, fileName, idPessoa) {

        var fd = new FormData();
        fd.append('uploadedFile', file);
        fd.append('fileName', fileName);
        fd.append('idPessoa', idPessoa);
        return $http.post(_path + "/upload/ARQUIVO_FOTO_PERFIL", fd, {
            transformRequest: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        })
    };

    var _getPerfilById = function (idAluno) {
        return $http.get(_path + "/download/perfil/aluno/" + idAluno);
    }

    return {
        upload: _upload,
        getPerfilById: _getPerfilById
    };
});
