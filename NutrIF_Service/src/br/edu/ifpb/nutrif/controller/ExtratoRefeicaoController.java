package br.edu.ifpb.nutrif.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.edu.ifpb.nutrif.dao.CursoDAO;
import br.edu.ifpb.nutrif.exception.ErrorFactory;
import br.edu.ifpb.nutrif.exception.SQLExceptionNutrIF;
import br.edu.ifpb.nutrif.util.BancoUtil;
import br.edu.ifpb.nutrif.validation.Validate;
import br.edu.ladoss.entity.Curso;
import br.edu.ladoss.entity.Error;
import br.edu.ladoss.enumeration.TipoRole;

@Path("extratorefeicao")
public class ExtratoRefeicaoController {

	@RolesAllowed({TipoRole.ADMIN})
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response insert(Curso curso) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.curso(curso);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {			
				
				//Inserir o Curso.
				Integer idCurso = CursoDAO.getInstance().insert(curso);
				
				if (idCurso != BancoUtil.ID_VAZIO) {

					// Opera��o realizada com sucesso.
					builder.status(Response.Status.OK);
					builder.entity(curso);
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
	@GET
	@Produces("application/json")
	public List<Curso> getAll() {
		
		List<Curso> cursos = new ArrayList<Curso>();
		
		cursos = CursoDAO.getInstance().getAll();
		
		return cursos;
	}
	
	@PermitAll
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getById(@PathParam("id") int idCurso) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {

			Curso curso = CursoDAO.getInstance().getById(idCurso); 
			
			builder.status(Response.Status.OK);
			builder.entity(curso);

		} catch (SQLExceptionNutrIF qme) {

			Error erro = new Error();
			erro.setCodigo(qme.getErrorCode());
			erro.setMensagem(qme.getMessage());

			builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro);
		}

		return builder.build();
	}
	
	/**
	 * Atualizar dados de um Curso.
	 * 
	 * @param curso
	 * @return
	 */
	@RolesAllowed({TipoRole.ADMIN})
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Response update(Curso curso) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());
		
		// Valida��o dos dados de entrada.
		int validacao = Validate.curso(curso);
		
		if (validacao == Validate.VALIDATE_OK) {
			
			try {
				
				//Atualizar o Curso.
				curso = CursoDAO.getInstance().update(curso);
				
				if (curso != null) {

					// Opera��o realizada com sucesso.
					builder.status(Response.Status.OK);					
					builder.entity(curso);
				}
			
			} catch (SQLExceptionNutrIF exception) {

				builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
						exception.getError());			
			} 
		}				
		
		return builder.build();		
	}
	
	/**
	 * Listar Curso(s) pelo nome.
	 * 
	 * @param nome
	 * @return
	 */
	@PermitAll
	@GET
	@Path("/nome/{nome}")
	@Produces("application/json")
	public Response listByNome(@PathParam("nome") String nome) {
		
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		List<Curso> cursos = new ArrayList<Curso>();
		
		try {

			cursos = CursoDAO.getInstance().listByNome(nome);
			
			builder.status(Response.Status.OK);
			builder.entity(cursos);

		} catch (SQLExceptionNutrIF exception) {

			builder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
					exception.getError());
		}

		return builder.build();
	}	
}
