package br.edu.ifpb.nutrif.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import br.edu.ifpb.nutrif.exception.SQLExceptionNutrIF;
import br.edu.ifpb.nutrif.hibernate.HibernateUtil;
import br.edu.ifpb.nutrif.util.BancoUtil;
import br.edu.ladoss.entity.Arquivo;
import br.edu.ladoss.entity.DiaRefeicao;

public class ArquivoDAO extends GenericDao<Integer, Arquivo> {

	private static Logger logger = LogManager.getLogger(ArquivoDAO.class);
	
	private static ArquivoDAO instance;
	
	public static ArquivoDAO getInstance() {		
		instance = new ArquivoDAO();		
		return instance;
	}
	
	public Arquivo getByNomeSistema(String nomeSistemaArquivo) {
		
		Arquivo arquivo = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			
			String hql = "from Arquivo as a"
					+ " where a.nomeSistemaArquivo = :nomeSistemaArquivo";
			
			Query query = session.createQuery(hql);
			query.setParameter("nomeSistemaArquivo", nomeSistemaArquivo);
			
			arquivo = (Arquivo) query.uniqueResult();
	        
		} catch (HibernateException hibernateException) {
			
			session.getTransaction().rollback();
			
			throw new SQLExceptionNutrIF(hibernateException);
			
		} finally {
		
			session.close();
		}
		
		return arquivo;		
	}
	
	public List<Arquivo> getByIdPessoa(Integer idPessoa) {
		
		List<Arquivo> arquivos = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			
			String hql = "from Arquivo as a"
					+ " where a.submetedor.id = :idPessoa"
					+ " and a.ativo = :ativo";
			
			Query query = session.createQuery(hql);
			query.setParameter("idPessoa", idPessoa);
			query.setParameter("ativo", BancoUtil.ATIVO);
			
			arquivos = (List<Arquivo>) query.list();
	        
		} catch (HibernateException hibernateException) {
			
			session.getTransaction().rollback();
			
			throw new SQLExceptionNutrIF(hibernateException);
			
		} finally {
		
			session.close();
		}
		
		return arquivos;		
	}
	
	@Override
	public List<Arquivo> getAll() throws SQLExceptionNutrIF {
		return super.getAll("Arquivo.getAll");
	}

	@Override
	public Class<?> getEntityClass() {
		return Arquivo.class;
	}

	@Override
	public Arquivo find(Arquivo entity) throws SQLExceptionNutrIF {
		// TODO Auto-generated method stub
		return null;
	}
}
