package br.edu.ifpb.nutrif.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import br.edu.ifpb.nutrif.exception.SQLExceptionNutrIF;
import br.edu.ifpb.nutrif.hibernate.HibernateUtil;
import br.edu.ladoss.entity.Aluno;

public class AlunoDAO extends GenericDao<Integer, Aluno> {

	private static Logger logger = LogManager.getLogger(AlunoDAO.class);
	
	private static AlunoDAO instance;
	
	public static AlunoDAO getInstance() {		
		instance = new AlunoDAO();		
		return instance;
	}

	public Aluno getByNameAndRegistration(String nome, String matricula) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Aluno aluno = null;
		List<Aluno> alunos;
		Query query;
		
		try {
		
			session.beginTransaction();
			
			query = session.createQuery("from Aluno"
					+ " where nome like :nome"
					+ " and matricula = :matricula");
			query.setParameter("nome", nome);
			query.setParameter("matricula", matricula);

			alunos = (List<Aluno>) query.list();
			
			if (alunos.isEmpty()) {
				
				alunos = null;
				
			} else {
				
				aluno = alunos.get(0);
			}
	        
		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			session.getTransaction().rollback();
			
		} finally {
		
			session.close();
		}
		
		return aluno;
	}

	@Override
	public List<Aluno> getAll() throws SQLExceptionNutrIF {
		return super.getAll("Aluno.getAll");
	}

	@Override
	public Class<?> getEntityClass() {		
		return Aluno.class;
	}
}
