package br.edu.ifpb.nutrif.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.edu.ifpb.nutrif.dao.DiaDAO;
import br.edu.ifpb.nutrif.dao.DiaRefeicaoDAO;
import br.edu.ifpb.nutrif.dao.FuncionarioDAO;
import br.edu.ifpb.nutrif.dao.PretensaoRefeicaoDAO;
import br.edu.ifpb.nutrif.dao.RefeicaoDAO;
import br.edu.ifpb.nutrif.dao.RefeicaoRealizadaDAO;
import br.edu.ifpb.nutrif.exception.ErrorFactory;
import br.edu.ifpb.nutrif.exception.SQLExceptionNutrIF;
import br.edu.ifpb.nutrif.util.DateUtil;
import br.edu.ifpb.nutrif.validation.Validate;
import br.edu.ladoss.entity.ConfirmaRefeicaoDia;
import br.edu.ladoss.entity.Dia;
import br.edu.ladoss.entity.DiaRefeicao;
import br.edu.ladoss.entity.Error;
import br.edu.ladoss.entity.Funcionario;
import br.edu.ladoss.entity.MapaRefeicaoRealizada;
import br.edu.ladoss.entity.PretensaoRefeicao;
import br.edu.ladoss.entity.Refeicao;
import br.edu.ladoss.entity.RefeicaoRealizada;

@Path("refeicaorealizada")
public class RefeicaoRealizadaController {

	/**
	 * 
	 * @param refeicaoRealizada
	 * @return
	 */	
	@PermitAll
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
				
				// Recuperar o Cronograma da Refei��o.
				int idDiaRefeicao = refeicaoRealizada.getConfirmaRefeicaoDia()
						.getDiaRefeicao().getId();
				DiaRefeicao diaRefeicao = DiaRefeicaoDAO
						.getInstance().getById(idDiaRefeicao);
				
				if (diaRefeicao != null) {
					
					// Data e hora atual.
					Date agora = new Date();
					
					// Pretens�o
					PretensaoRefeicao pretensaoRefeicao = PretensaoRefeicaoDAO
							.getInstance().getPretensaoRefeicaoByDiaRefeicao(
									idDiaRefeicao, agora);
					
					// Analise para lan�amento obrigat�rio da prentes�o.
					if ((!diaRefeicao.getRefeicao().isPrevistoPretensao() 
							&& pretensaoRefeicao == null) ||
							(!diaRefeicao.getRefeicao().isPrevistoPretensao() 
									&& pretensaoRefeicao != null) ||
							(diaRefeicao.getRefeicao().isPrevistoPretensao() 
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
						boolean success = RefeicaoRealizadaDAO.getInstance()
								.insertOrUpdate(refeicaoRealizada);					
						
						if (success) {
							
							// Opera��o realizada com sucesso.
							builder.status(Response.Status.OK);
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
	
	@PermitAll
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
					MapaRefeicaoRealizada mapaRefeicaoRealizada = new MapaRefeicaoRealizada();
					mapaRefeicaoRealizada.setQuantidade(
							Integer.valueOf(quantidadeDia.toString()));
					mapaRefeicaoRealizada.setDataInicio(dataRefeicaoRealizada);
					mapaRefeicaoRealizada.setDataFim(dataRefeicaoRealizada);
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
	
	@PermitAll
	@POST
	@Path("/mapa/consultar")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getMapaRefeicoesRealizadas(
			MapaRefeicaoRealizada mapaRefeicoesRealizadas) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.VALIDATE_OK; //TODO: Validate.pretensaoRefeicao(pretensaoRefeicao);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {			
				
				List<RefeicaoRealizada> refeicoesRealizadas = RefeicaoRealizadaDAO
						.getInstance().getMapaRefeicoesRaelizadas(
								mapaRefeicoesRealizadas);
				
				mapaRefeicoesRealizadas.setRefeicoesRealizadas(refeicoesRealizadas);
				
				builder.status(Response.Status.OK).entity(mapaRefeicoesRealizadas);
				
			
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
