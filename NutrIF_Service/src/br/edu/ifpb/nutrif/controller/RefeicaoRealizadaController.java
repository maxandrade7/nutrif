package br.edu.ifpb.nutrif.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.edu.ifpb.nutrif.dao.AlunoDAO;
import br.edu.ifpb.nutrif.dao.DiaDAO;
import br.edu.ifpb.nutrif.dao.DiaRefeicaoDAO;
import br.edu.ifpb.nutrif.dao.FuncionarioDAO;
import br.edu.ifpb.nutrif.dao.PretensaoRefeicaoDAO;
import br.edu.ifpb.nutrif.dao.RefeicaoDAO;
import br.edu.ifpb.nutrif.dao.RefeicaoRealizadaDAO;
import br.edu.ifpb.nutrif.exception.ErrorFactory;
import br.edu.ifpb.nutrif.exception.SQLExceptionNutrIF;
import br.edu.ifpb.nutrif.util.BancoUtil;
import br.edu.ifpb.nutrif.util.DateUtil;
import br.edu.ifpb.nutrif.util.StringUtil;
import br.edu.ifpb.nutrif.validation.Validate;
import br.edu.ladoss.entity.Aluno;
import br.edu.ladoss.entity.ConfirmaRefeicaoDia;
import br.edu.ladoss.entity.Dia;
import br.edu.ladoss.entity.DiaRefeicao;
import br.edu.ladoss.entity.Error;
import br.edu.ladoss.entity.Funcionario;
import br.edu.ladoss.entity.MapaRefeicao;
import br.edu.ladoss.entity.PeriodoRefeicaoRealizada;
import br.edu.ladoss.entity.PretensaoRefeicao;
import br.edu.ladoss.entity.Refeicao;
import br.edu.ladoss.entity.RefeicaoRealizada;
import br.edu.ladoss.enumeration.TipoRole;

@Path("refeicaorealizada")
public class RefeicaoRealizadaController {

	private static Logger logger = LogManager.getLogger(RefeicaoRealizadaController.class);
	
	/**
	 * 
	 * @param refeicaoRealizada
	 * @return
	 */	
	@RolesAllowed({TipoRole.ADMIN, TipoRole.INSPETOR})
	@POST
	@Path("/inserir")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insert(RefeicaoRealizada refeicaoRealizada) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.refeicaoRealizada(refeicaoRealizada);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {			
				
				// CPF para atualiza��o do cadastro do Aluno.
				String cpf = refeicaoRealizada.getConfirmaRefeicaoDia().getDiaRefeicao().getAluno() != null ?
						refeicaoRealizada.getConfirmaRefeicaoDia().getDiaRefeicao().getAluno().getCpf(): null;
				
				// Recuperar o Cronograma da Refei��o.
				int idDiaRefeicao = refeicaoRealizada.getConfirmaRefeicaoDia()
						.getDiaRefeicao().getId();
				DiaRefeicao diaRefeicao = DiaRefeicaoDAO
						.getInstance().getById(idDiaRefeicao);
				
				if (diaRefeicao != null) {
					
					// Data e hora atual.
					Date agora = new Date();
					
					// Atualizar CPF do Aluno.
					Aluno aluno = diaRefeicao.getAluno();
					atualizarAlunoCPF(aluno, cpf);
					
					// Pretens�o
					PretensaoRefeicao pretensaoRefeicao = PretensaoRefeicaoDAO
							.getInstance().getPretensaoRefeicaoByDiaRefeicao(
									idDiaRefeicao, agora);
					
					// Analise para lan�amento obrigat�rio da prentes�o.
					if ((!diaRefeicao.getEdital().isPrevistoPretensao() 
							&& pretensaoRefeicao == null) ||
							(!diaRefeicao.getEdital().isPrevistoPretensao() 
									&& pretensaoRefeicao != null) ||
							(diaRefeicao.getEdital().isPrevistoPretensao() 
								&& pretensaoRefeicao != null)) {
						
						// Confirma��o da refei��o
						ConfirmaRefeicaoDia confirmaRefeicaoDia = 
								new ConfirmaRefeicaoDia();
						confirmaRefeicaoDia.setDiaRefeicao(diaRefeicao);
						confirmaRefeicaoDia.setDataRefeicao(agora);
						refeicaoRealizada.setConfirmaRefeicaoDia(
								confirmaRefeicaoDia);
						
						// Hora da refei��o
						refeicaoRealizada.setHoraRefeicao(agora);
						
						// Funcion�rio respons�vel registro da refei��o realizada.
						Funcionario inspetor = FuncionarioDAO.getInstance().getById(
								refeicaoRealizada.getInspetor().getId());
						refeicaoRealizada.setInspetor(inspetor);
						
						//Inserir a Refei��o Realizada.
						int idRefeicaoRealizada = RefeicaoRealizadaDAO.getInstance()
								.insert(refeicaoRealizada);					
						logger.info("RefeicaoRealizada: " + idRefeicaoRealizada + "->" + refeicaoRealizada);
						
						if (idRefeicaoRealizada != BancoUtil.ID_VAZIO) {
							
							// Opera��o realizada com sucesso.
							builder.status(Response.Status.OK);
						} else {
							
							// Mensagem de pretens�o obrigat�ria.
							builder.status(Response.Status.NOT_MODIFIED).entity(
									ErrorFactory.getErrorFromIndex(
											ErrorFactory.REFEICAO_NAO_REALIZADA));
						}
					
					} else {
						
						// Mensagem de pretens�o obrigat�ria.
						builder.status(Response.Status.NOT_ACCEPTABLE).entity(
								ErrorFactory.getErrorFromIndex(
										ErrorFactory.PRETENSAO_REFEICAO_NAO_ENCONTRADA));
					}					
					
				} else {
					
					builder.status(Response.Status.NOT_FOUND).entity(
							ErrorFactory.getErrorFromIndex(
									ErrorFactory.ID_DIA_REFEICAO_INVALIDO));
				}				
			
			} catch (SQLExceptionNutrIF exception) {

				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());			
			}
			
		} else {
			
			Error erro = ErrorFactory.getErrorFromIndex(validacao);
			builder.status(Response.Status.NOT_ACCEPTABLE).entity(erro);
		}					
		
		return builder.build();		
	}
	
	private void atualizarAlunoCPF(Aluno aluno, String cpf) {
		
		if (!StringUtil.isEmptyOrNull(cpf)) {			
			aluno.setCpf(cpf);			
			AlunoDAO.getInstance().update(aluno);
		}		
	}

	@DenyAll
	@GET
	@Path("/listar")
	@Produces("application/json")
	public List<RefeicaoRealizada> getAll() {
		
		List<RefeicaoRealizada> refeicaoRealizada = 
				new ArrayList<RefeicaoRealizada>();
		
		refeicaoRealizada = RefeicaoRealizadaDAO.getInstance().getAll();
		
		return refeicaoRealizada;
	}
	
	@RolesAllowed({TipoRole.ADMIN, TipoRole.INSPETOR})
	@GET
	@Path("/id/{id}")
	@Produces("application/json")
	public Response getRefeicaoRealizadaById(
			@PathParam("id") int idRefeicaoRealizada) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {

			RefeicaoRealizada refeicaoRealizada = RefeicaoRealizadaDAO
					.getInstance().getById(idRefeicaoRealizada); 
			
			if (refeicaoRealizada != null) {
				
				// Refei��o Realizada encontrada.
				builder.status(Response.Status.OK);
				builder.entity(refeicaoRealizada);
				
			} else {
				
				builder.status(Response.Status.NOT_FOUND).entity(
						ErrorFactory.getErrorFromIndex(
								ErrorFactory.REFEICAO_REALIZADA_NAO_ENCONTRADA));
			}		

		} catch (SQLExceptionNutrIF exception) {

			builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
					exception.getError());			
		}

		return builder.build();
	}
	
	/**
	 * Consultar refei��o realizada pelo dia da refei��o. � necess�rio informar a 
	 * Refei��o e o Dia para a consulta. O Dia ser� transformado em data referente a
	 * semana vigente. 
	 * 
	 * @param diaRefeicao
	 * @return
	 */
	@PermitAll
	@POST
	@Path("/quantificar")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getQuantidadeRefeicoesRealizadas(DiaRefeicao diaRefeicao) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.quantidadeRefeicaoRealizada(diaRefeicao);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {
				
				// Recurar dados da Refei��o.
				Refeicao refeicao = RefeicaoDAO.getInstance().getById(
						diaRefeicao.getRefeicao().getId());
				diaRefeicao.setRefeicao(refeicao);
				
				// Id do Dia da Refeic�o Realizada.
				int idDia = diaRefeicao.getDia().getId();
				
				// Recuperar Dia.
				Dia dia = DiaDAO.getInstance().getById(idDia);
				
				// Dia da semana da(s) Refeic�o(�es) Realizada(s).
				Date dataRefeicaoRealizada = DateUtil.getDateOfDayWeek(idDia);
						
				if (refeicao != null && dia != null) {
					
					// Consultar Refeic�o(�es) Realizada(s).
					Long quantidadeDia = RefeicaoRealizadaDAO.getInstance()
							.getQuantidadeDiaRefeicaoRealizada(refeicao,
									dataRefeicaoRealizada);	
					
					// Mapa quantitativo da(s) Refeic�o(�es) Realizada(s).
					MapaRefeicao<RefeicaoRealizada> mapaRefeicaoRealizada = 
							new MapaRefeicao<RefeicaoRealizada>();
					mapaRefeicaoRealizada.setQuantidade(
							Integer.valueOf(quantidadeDia.toString()));
					mapaRefeicaoRealizada.setData(dataRefeicaoRealizada);
					mapaRefeicaoRealizada.setDia(dia);			
					
					builder.status(Response.Status.OK).entity(
							mapaRefeicaoRealizada);
				}				
			
			} catch (SQLExceptionNutrIF exception) {

				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());			
			}
			
		} else {
			
			Error erro = ErrorFactory.getErrorFromIndex(validacao);
			builder.status(Response.Status.NOT_ACCEPTABLE).entity(erro);
		}
		
		return builder.build();
	}
	
	/**
	 * Consultar refei��o(�es) realizada por per�odo.
	 * 
	 * @param periodoRefeicaoRealizada
	 * @return
	 */
	@RolesAllowed({TipoRole.ADMIN})
	@POST
	@Path("/consultar/mapa")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getMapaRefeicoesRealizadas(
			PeriodoRefeicaoRealizada periodoRefeicaoRealizada) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.periodoRefeicaoRealizada(periodoRefeicaoRealizada);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {
				
				List<MapaRefeicao<RefeicaoRealizada>> mapasRefeicoesRealizadas = 
						new ArrayList<MapaRefeicao<RefeicaoRealizada>>();
				
				// Data entre o intervalo de dataInicio e dataFim.
				List<Date> datas = DateUtil.getDaysBetweenDates(
						periodoRefeicaoRealizada.getDataInicio(), 
						periodoRefeicaoRealizada.getDataFim());
				
				Refeicao refeicao = RefeicaoDAO.getInstance().getById(
						periodoRefeicaoRealizada.getRefeicao().getId());				
				
				if (refeicao != null) {	
					
					for (Date data: datas) {
						
						// Consulta dos dias das refei��es.
						List<RefeicaoRealizada> refeicoesRealizadas = RefeicaoRealizadaDAO
								.getInstance().getMapaRefeicoesRealizadas(
										refeicao, data);
						
						// Inicializa o mapa para consulta dos dias das refei��es.
						MapaRefeicao<RefeicaoRealizada> mapaRefeicaoRealizada = 
								new MapaRefeicao<RefeicaoRealizada>();				
						mapaRefeicaoRealizada.setRefeicao(
								periodoRefeicaoRealizada.getRefeicao());
						mapaRefeicaoRealizada.setData(data);						
						mapaRefeicaoRealizada.setLista(refeicoesRealizadas);
						mapaRefeicaoRealizada.setQuantidade(
								refeicoesRealizadas.size());
						
						mapasRefeicoesRealizadas.add(mapaRefeicaoRealizada);
					}				
					
					builder.status(Response.Status.OK).entity(
							mapasRefeicoesRealizadas);
				} else {
					
					builder.status(Response.Status.NOT_FOUND).entity(
							ErrorFactory.getErrorFromIndex(
									ErrorFactory.ID_REFEICAO_INVALIDA));					
				}								
			
			} catch (SQLExceptionNutrIF exception) {

				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());			
			}
			
		} else {
			
			Error erro = ErrorFactory.getErrorFromIndex(validacao);
			builder.status(Response.Status.NOT_ACCEPTABLE).entity(erro);
		}
		
		return builder.build();	
	}	
	
	/**
	 * Listar Refei��es Realizadas por Dia e Refei��o para todos os Editais vigentes.
	 * 
	 * @param idDia
	 * @return
	 */
	@RolesAllowed({TipoRole.ADMIN})
	@GET
	@Path("/consultar/mapa/dia/{idDia}/refeicao/{idRefeicao}")
	@Produces("application/json")
	public Response getMapaRefeicaoRealizadaByDiaRefeicao(@PathParam("idDia") Integer idDia, 
			@PathParam("idRefeicao") Integer idRefeicao) {

		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());		
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.listarRefeicaoRealizadaByDiaRefeicao(idDia, idRefeicao);

		if (validacao == Validate.VALIDATE_OK) {
			try {
				
				Dia dia = DiaDAO.getInstance().getById(idDia);
				
				Refeicao refeicao = RefeicaoDAO.getInstance().getById(idRefeicao);
				
				if (dia != null && refeicao != null) {
					
					// Recuperar o data do Dia.
					Date dataRefeicao = DateUtil.getDateOfDayWeek(dia.getId());
					
					// Listar Refei��es Realizadas por Data e Refei��o.
					List<RefeicaoRealizada> refeicoesRealizadas = RefeicaoRealizadaDAO.getInstance()
							.listDiaRefeicaoByDataRefeicao(dataRefeicao, idDia, idRefeicao);
					
					// Mapa para contabiliza��o e listagem.
					MapaRefeicao<RefeicaoRealizada> mapa = new MapaRefeicao<RefeicaoRealizada>();						
					mapa.setDia(dia);
					mapa.setRefeicao(refeicao);
					mapa.setData(dataRefeicao);
					mapa.setQuantidade(refeicoesRealizadas.size());
					mapa.setLista(refeicoesRealizadas);
					
					builder.status(Response.Status.OK);
					builder.entity(mapa);
				}				

			} catch (SQLExceptionNutrIF exception) {

				// Erro na manipula��o dos dados
				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());
			}

		} else {

			// Solicita��o fora do per�odo de uma refei��o.
			builder.status(Response.Status.NOT_ACCEPTABLE).entity(
					ErrorFactory.getErrorFromIndex(validacao));
		}

		return builder.build();
	}	
	
	@RolesAllowed({TipoRole.ADMIN})
	@GET
	@Path("/detalhar/edital/{idEdital}/aluno/{matricula}")
	@Produces("application/json")
	public Response listRefeicaoRealizadaByEditalAluno(@PathParam("idEdital") Integer idEdital, 
			@PathParam("matricula") String matricula) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.listarRefeicaoRealizadaByEditalAluno(idEdital, matricula);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {
					List<MapaRefeicao<RefeicaoRealizada>> mapas = new ArrayList<MapaRefeicao<RefeicaoRealizada>>();
				
					// Recuperar todo os Dias.
					List<DiaRefeicao> diasRefeicao = DiaRefeicaoDAO.getInstance().getAllVigentesByEditalAluno(
							idEdital, matricula);
					
					if (diasRefeicao != null && !diasRefeicao.isEmpty()) {
						
						for (DiaRefeicao diaRefeicao: diasRefeicao) {
							
							Dia dia = diaRefeicao.getDia();
							Integer idDia = dia.getId();
							
							List<RefeicaoRealizada> refeicoesRealizadas = RefeicaoRealizadaDAO.getInstance()
									.detalharRefeicoesRealizadasByEditalAluno(idEdital, matricula, idDia);
							
							MapaRefeicao<RefeicaoRealizada> mapa = new MapaRefeicao<RefeicaoRealizada>();						
							mapa.setDia(dia);
							mapa.setQuantidade(refeicoesRealizadas.size());
							mapa.setLista(refeicoesRealizadas);	
							
							mapas.add(mapa);
						}

						builder.status(Response.Status.OK);
						builder.entity(mapas);
					}					
			
			} catch (SQLExceptionNutrIF exception) {

				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());			
			}
			
		} else {
			
			Error erro = ErrorFactory.getErrorFromIndex(validacao);
			builder.status(Response.Status.NOT_ACCEPTABLE).entity(erro);
		}
		
		return builder.build();
	}
	
	@RolesAllowed({TipoRole.ADMIN})
	@GET
	@Path("/falta/{matricula}")
	@Produces("application/json")
	public Response listFalta(@PathParam("matricula") String matricula) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		int tresSemanas = 3;
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.VALIDATE_OK;
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {
					List<MapaRefeicao<RefeicaoRealizada>> mapasRefeicoesRealizadas = 
						new ArrayList<MapaRefeicao<RefeicaoRealizada>>();
				
					// Recuperar todos os dias de refei��o ativos
					List<DiaRefeicao> diasRefeicao = DiaRefeicaoDAO.getInstance()
							.getAllVigentesByAlunoMatricula(matricula);
					
					for (DiaRefeicao diaRefeicao: diasRefeicao) {
						// Calcular intervalo.
						List<Date> datas = DateUtil.getAllDatesPastOfDayWeek(tresSemanas, diaRefeicao.getDia().getId());

						// Consultas as reei��es realizadas nas �ltimas 3 semanas
						List<RefeicaoRealizada> refeicoesRealizadas = RefeicaoRealizadaDAO.getInstance()
								.listByDiaRefeicaoInData(diaRefeicao.getId(), datas);
						
						MapaRefeicao<RefeicaoRealizada> mapa = new MapaRefeicao<RefeicaoRealizada>();
						mapa.setDia(diaRefeicao.getDia());
						mapa.setEdital(diaRefeicao.getEdital());
						mapa.setAluno(diaRefeicao.getAluno());						
						mapa.setLista(refeicoesRealizadas);
						mapa.setQuantidade(refeicoesRealizadas.size());	
						
						mapasRefeicoesRealizadas.add(mapa);
					}
										
					builder.status(Response.Status.OK).entity(
							mapasRefeicoesRealizadas);
					
			} catch (SQLExceptionNutrIF exception) {

				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());			
			}
			
		} else {
			
			Error erro = ErrorFactory.getErrorFromIndex(validacao);
			builder.status(Response.Status.NOT_ACCEPTABLE).entity(erro);
		}
		
		return builder.build();
	}
}
