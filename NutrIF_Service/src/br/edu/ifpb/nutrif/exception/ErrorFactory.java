package br.edu.ifpb.nutrif.exception;

import java.util.HashMap;
import java.util.Map;

import br.edu.ladoss.entity.Error;

public class ErrorFactory {

	private ErrorFactory() {}

	/*
	 * Error status: Aluno.
	 */
	public static final int REGISTRO_DUPLICADO = 1;
	public static final int ALUNO_NAO_ENCONTRADO = 2;
	public static final int NOME_ALUNO_INVALIDO = 3;
	public static final int MATRICULA_ALUNO_INVALIDA = 4;
	public static final int CHAVE_CONFIRMACAO_INVALIDA = 5;
	public static final int NOME_MATRICULA_ALUNO_INVALIDOS = 6;
	public static final int ACESSO_ALUNO_NAO_PERMITIDO = 7;
	public static final int MATRICULA_ALUNO_DUPLICADA = 8;
	
	/*
	 * Error status: Curso.
	 */
	public static final int ID_CURSO_INVALIDO = 9;
	public static final int NOME_CURSO_INVALIDO = 10;
	
	/*
	 * Error status: Usu�rio.
	 */
	public static final int NOME_USUARIO_INVALIDO = 11;
	public static final int EMAIL_USUARIO_INVALIDO = 12;
	public static final int SENHA_USUARIO_INVALIDA = 13;
	public static final int KEY_CONFIRMATION_INVALIDA = 14;
	
	/*
	 * Error status: Dia da Refei��o.
	 */
	public static final int ID_DIA_REFEICAO_INVALIDO = 15;
	public static final int ID_ALUNO_INVALIDO = 16;
	public static final int ID_DIA_INVALIDO = 17;
	public static final int ID_REFEICAO_INVALIDA = 18;
	public static final int DIA_REFEICAO_DUPLICADO = 19;
	public static final int DIA_REFEICAO_NAO_DEFINIDO = 20;
	public static final int QUANTIDADE_BENEFICIARIOS_EXCEDENTE = 21;
	
	/*
	 * Confirma��o da Refei��o.
	 */
	public static final int CONFIRMACAO_REFEICAO_INVALIDA = 22;
	
	/*
	 * Pretens�o da Refei��o
	 */
	public static final int PRETENSAO_REFEICAO_NAO_ENCONTRADA = 23;
	public static final int PRETENSAO_REFEICAO_INVALIDA = 24;
	public static final int CONFIRMACAO_PRETENSAO_INVALIDA = 25;
	public static final int CHAVE_ACESSO_PRETENSAO_INVALIDA = 26;
	
	/*
	 * Realiza��o da Refei��o
	 */
	public static final int REFEICAO_REALIZADA_NAO_ENCONTRADA = 27;
	public static final int REFEICAO_JA_REALIZADA = 28;
	
	/*
	 * Funcion�rio
	 */
	public static final int CODIGO_FUNCIONARIO_INSPETOR_INVALIDO = 29;
	public static final int ID_FUNCIONARIO_INVALIDO = 30;
	public static final int ID_RESPONSAVEL_INVALIDO = 31;
	
	/*
	 * Pessoa
	 */
	public static final int CHAVE_AUTORIZACAO_PESSOA_INVALIDA = 32;
	public static final int ID_PESSOA_INVALIDO = 33;
	
	public static final int IMPOSSIVEL_CRIPTOGRAFAR_VALOR = 34;
	
	/*
	 * Arquivo
	 */
	public static final int TIPO_ARQUIVO_INVALIDO = 35;
	public static final int NOME_ARQUIVO_INVALIDO = 36;
	public static final int TAMANHO_ARQUIVO_INVALIDO = 37;
	public static final int FORMULARIO_ARQUIVO_INVALIDO = 38;
	public static final int ARQUIVO_PERFIL_INVALIDO = 39;
	
	/*
	 * Roles
	 */
	public static final int ROLES_INVALIDAS = 40;
	public static final int NOME_ROLE_INVALIDO = 41;
	public static final int DATA_INVALIDA = 42;
	
	/*
	 * Campus
	 */
	public static final int ID_CAMPUS_INVALIDO = 43;
	
	/*
	 * Edital
	 */
	public static final int ID_EDITAL_INVALIDO = 44;
	public static final int QTD_COMTEMPLADO_INVALIDO = 45;
	public static final int INTERVALO_DATA_INVALIDO = 46;
	
	/*
	 * Evento
	 */
	public static final int ID_EVENTO_INVALIDO = 47;
	public static final int NOME_EVENTO_INVALIDO = 48;
	public static final int DESCRICAO_EVENTO_INVALIDO = 49;
	
	/*
	 * Refei��o
	 */
	public static final int PERIODO_REFEICAO_INVALIDO = 50;
	public static final int TIPO_REFEICAO_INVALIDO = 51;
	public static final int DIA_PREVISTO_PRETENSAO_INVALIDO = 52;
	public static final int PERIODO_PREVISAO_PRETENSAO = 53;
	
	/*
	 * Per�odo, turma e turno.
	 */
	public static final int ID_PERIODO_INVALIDO = 54;
	public static final int ID_TURMA_INVALIDO = 55;
	public static final int ID_TURNO_INVALIDO = 56;
	
	public static final int DIA_REFEICAO_NAO_DEFINIDO_EDITAL = 57;
	public static final int DIA_REFEICAO_NAO_DEFINIDO_ALUNO = 58;	
	
	public static final int REFEICAO_NAO_REALIZADA = 59;
	
	public static final int RESPONSAVEL_ADMINISTRADOR_INVALIDO = 61;
	
	public static final int CPF_INVALIDO = 62;
	
	public static final int ERRO_INTERNO_SERVICO = 63;
	
	/*
	 * Mapa de erros: c�digo e mensagem.
	 */
	private static final Map<Integer, String> mapErrors = generateErrorMapping();

	private final static Map<Integer, String> generateErrorMapping() {
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();

		hashMap.put(REGISTRO_DUPLICADO, "Registro duplicado.");
		hashMap.put(ALUNO_NAO_ENCONTRADO, "Aluno n�o encontrado.");		
		hashMap.put(NOME_ALUNO_INVALIDO, "Nome do aluno inv�lido.");
		hashMap.put(CHAVE_CONFIRMACAO_INVALIDA, "Chave confirma��o inv�lida.");
		hashMap.put(MATRICULA_ALUNO_INVALIDA, "Matr�cula do aluno inv�lida.");		
		hashMap.put(ID_CURSO_INVALIDO, "Curso inv�lido.");
		hashMap.put(NOME_CURSO_INVALIDO, "Nome do curso inv�lido.");
		hashMap.put(NOME_USUARIO_INVALIDO, "Nome do usu�rio inv�lido.");
		hashMap.put(EMAIL_USUARIO_INVALIDO, "E-mail do usu�rio inv�lido.");
		hashMap.put(SENHA_USUARIO_INVALIDA, "Senha do usu�rio inv�lida.");
		hashMap.put(ID_ALUNO_INVALIDO, "Aluno inv�lido.");
		hashMap.put(ID_DIA_INVALIDO, "Dia inv�lido.");
		hashMap.put(ID_REFEICAO_INVALIDA, "Refei��o inv�lida.");
		hashMap.put(IMPOSSIVEL_CRIPTOGRAFAR_VALOR, "Imposs�vel criptografar valor.");
		hashMap.put(KEY_CONFIRMATION_INVALIDA, "Chave de confirma��o inv�lida.");
		hashMap.put(ID_DIA_REFEICAO_INVALIDO, "Dia da Refei��o inv�lido.");
		hashMap.put(CONFIRMACAO_REFEICAO_INVALIDA, "Confirma��o da refei��o inv�lida.");
		hashMap.put(NOME_MATRICULA_ALUNO_INVALIDOS, "Nome e matr�cula do aluno inv�lidos.");
		hashMap.put(PRETENSAO_REFEICAO_NAO_ENCONTRADA, "Pretens�o da refei��o n�o encontrada.");
		hashMap.put(ACESSO_ALUNO_NAO_PERMITIDO, "Acesso n�o permitido. Dados de login n�o conferem.");
		hashMap.put(REFEICAO_REALIZADA_NAO_ENCONTRADA, "Refei��o realizada n�o encotrada.");
		hashMap.put(CONFIRMACAO_PRETENSAO_INVALIDA, "Confirma��o de pretens�o inv�lida.");
		hashMap.put(CHAVE_ACESSO_PRETENSAO_INVALIDA, "Chave de acesso da pretens�o da refei��o inv�lida.");
		hashMap.put(CODIGO_FUNCIONARIO_INSPETOR_INVALIDO, "C�digo do inspetor inv�lido.");
		hashMap.put(CHAVE_AUTORIZACAO_PESSOA_INVALIDA, "Chave de autoriza��o de acesso para usu�rio inv�lida.");
		hashMap.put(PRETENSAO_REFEICAO_INVALIDA, "Pretens�o de refei��o inv�lida.");
		hashMap.put(ID_FUNCIONARIO_INVALIDO, "Funcion�rio inv�lido.");
		hashMap.put(TIPO_ARQUIVO_INVALIDO, "Tipo do arquivo inv�lido.");		
		hashMap.put(NOME_ARQUIVO_INVALIDO, "Nome arquivo inv�lido.");
		hashMap.put(ID_PESSOA_INVALIDO, "Identificador da pessoa inv�lido.");
		hashMap.put(TAMANHO_ARQUIVO_INVALIDO, "Tamanho do arquivo inv�lido.");
		hashMap.put(FORMULARIO_ARQUIVO_INVALIDO, "Formul�rio com dados da submiss�o do inv�lido.");
		hashMap.put(MATRICULA_ALUNO_DUPLICADA, "Matr�cula do aluno duplicada.");
		hashMap.put(ARQUIVO_PERFIL_INVALIDO, "Arquivo do perfil n�o encontrado.");
		hashMap.put(ROLES_INVALIDAS, "Perfis de usu�rio n�o informados.");
		hashMap.put(NOME_ROLE_INVALIDO, "Nome do perfil inv�lido.");
		hashMap.put(DIA_REFEICAO_DUPLICADO, "Dia de refei��o duplicado para o Aluno.");
		hashMap.put(DATA_INVALIDA, "Data inv�lida.");
		hashMap.put(ID_CAMPUS_INVALIDO, "Campus inv�lido.");
		hashMap.put(QTD_COMTEMPLADO_INVALIDO, "Quantidade de contemplados inv�lido.");
		hashMap.put(INTERVALO_DATA_INVALIDO, "Intervalo de datas inv�lido.");
		hashMap.put(ID_EDITAL_INVALIDO, "Edital inv�lido.");
		hashMap.put(ID_EVENTO_INVALIDO, "Evento inv�lido.");
		hashMap.put(PERIODO_REFEICAO_INVALIDO, "Per�odo de Refei��o inv�lido.");
		hashMap.put(REFEICAO_JA_REALIZADA, "Refei��o j� realizada.");
		hashMap.put(DIA_REFEICAO_NAO_DEFINIDO, "Dia de Refei��o n�o definido.");
		hashMap.put(DIA_REFEICAO_NAO_DEFINIDO_ALUNO, "Dia de Refei��o n�o definido para o Aluno.");
		hashMap.put(DIA_REFEICAO_NAO_DEFINIDO_EDITAL, "Dia de Refei��o n�o definido para o Edital.");
		hashMap.put(NOME_EVENTO_INVALIDO, "Nome do evento inv�lido.");
		hashMap.put(DESCRICAO_EVENTO_INVALIDO, "Descri��o do evento inv�lido.");
		hashMap.put(TIPO_REFEICAO_INVALIDO, "Tipo que descreve a refei��o inv�lido.");	
		hashMap.put(DIA_PREVISTO_PRETENSAO_INVALIDO, "Quantidade de dia(s) previtos para a pretens�o da refei��o inv�lido.");
		hashMap.put(PERIODO_PREVISAO_PRETENSAO, "Per�odo de Previs�o da Pretens�o inv�lido");
		hashMap.put(ERRO_INTERNO_SERVICO, "Ops! Aconteceu algo estranho no NutrIF. Contacte os administradores.");
		hashMap.put(QUANTIDADE_BENEFICIARIOS_EXCEDENTE, "Inser��o do novo dia de refei��o excede a quantidade prevista para o Edital.");				
		hashMap.put(ID_PERIODO_INVALIDO, "Per�odo da Turma inv�lido.");
		hashMap.put(ID_TURMA_INVALIDO, "Turma do Curso inv�lida.");
		hashMap.put(ID_TURNO_INVALIDO, "Turno da Turma inv�lido.");
		hashMap.put(REFEICAO_NAO_REALIZADA, "Refei��o n�o realizada.");
		hashMap.put(RESPONSAVEL_ADMINISTRADOR_INVALIDO, "N�o � poss�vel cadastrar Edital na responsabilidade do Administrador.");
		hashMap.put(CPF_INVALIDO, "Cpf inv�lido.");
		
		return hashMap;
	}

	public static final Error getErrorFromIndex(int index) {
		
		Error error = new Error();
		error.setCodigo(index);
		error.setMensagem(mapErrors.get(index));
		
		return error;
	}
}
