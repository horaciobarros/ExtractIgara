package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.jway.igaratinga.entidadesOrigem.DadosLivroPrestador;
import br.com.jway.igaratinga.util.HibernateUtil;

public class DadosLivroPrestadorDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public DadosLivroPrestadorDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public DadosLivroPrestador save(DadosLivroPrestador dlp) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(dlp);
			session.getTransaction().commit();
			return dlp;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public DadosLivroPrestador update(DadosLivroPrestador dlp) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(dlp);
			session.getTransaction().commit();
			return dlp;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public DadosLivroPrestador findByPrestadorAndCodigoAtividade(String inscricaoPrestador, String codigoAtividade) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from DadosLivroPrestador dlp  " + " where dlp.cnpjPrestador like :cnpj and dlp.codigoAtividadeMunipal like :codigoAtividade ")
				.setParameter("cnpj", inscricaoPrestador)
				.setParameter("codigoAtividade", codigoAtividade);

		try {
			List<DadosLivroPrestador> dlps = query.list();

			if (dlps.size() > 0) {
				return dlps.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}
	
	public DadosLivroPrestador findById(Long id) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from DadosLivroPrestador dlp  " + " where dlp.idCodigo = " + id);

		try {
			List<DadosLivroPrestador> dlps = query.list();

			if (dlps.size() > 0) {
				return dlps.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}

	public boolean exists(Long id) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from DadosLivroPrestador dlp  where dlp.idCodigo = " + id);

		try {
			List<DadosLivroPrestador> dlps = query.list();

			if (dlps.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return false;
	}
}
