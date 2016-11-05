package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.util.HibernateUtil;

public class NotasFiscaisDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public NotasFiscaisDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public NotasFiscais save(NotasFiscais nf) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(nf);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return nf;
	}
	
	public NotasFiscais update(NotasFiscais nf) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(nf);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return nf;
		
	}

	public List<NotasFiscais> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscais c where hash is null").setFirstResult(0)
				.setMaxResults(500);
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<NotasFiscais> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscais set hash = '" + hash + "' where ");

		for (NotasFiscais c : listaAtualizados) {
			builder.append("id = " + c.getId() + " or ");
		}

		String sql = builder.toString();
		sql = sql.toString().substring(0, sql.length() - 4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public NotasFiscais findByNumeroDocumentoInscricaoPrestador(String numeroNota, String inscricaoPrestador) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscais nf where numeroNota = '" + numeroNota
				+ "' and inscricaoPrestador = '" + inscricaoPrestador + "'");
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();
		if (lista.size() > 0) {
			return lista.get(0);
		}
		return null;
	}
	
	public NotasFiscais findByNumeroDocumentoInscricaoPrestadorNumeroVerificacao(String numeroNota, String inscricaoPrestador, String numeroVerificacao) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscais nf where numeroNota = '" + numeroNota
				+ "' and inscricaoPrestador = '" + inscricaoPrestador + "'"
		+ "' and numeroVerificacao = '" + numeroVerificacao + "'");
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();
		if (lista.size() > 0) {
			return lista.get(0);
		}
		return null;
	}
	
	
	public List<NotasFiscais> findAll() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscais");
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}
	
	public List<NotasFiscais> findSubstituidas() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscais nf where nf.numeroNotaFiscalSubstituta is not null");
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public NotasFiscais findByNumeroVerificacaoInscricaoPrestador(String codigoVerificacao, String inscricaoPrestador) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscais nf where nf.numeroVerificacao like :num and nf.inscricaoPrestador = :insc")
				.setParameter("num", "%"+codigoVerificacao+"%")
				.setParameter("insc", inscricaoPrestador);
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();

		if (lista !=null && lista.size()>0){
			return lista.get(0);
		}
		else{
			return null;
		}
	}


}
