package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.jway.igaratinga.model.Tomadores;
import br.com.jway.igaratinga.util.HibernateUtil;

public class TomadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public TomadoresDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Tomadores findByInscricao(String inscricaoTomador, String inscricaoPrestador) {
		Query query = sessionFactory.openSession()
				.createQuery("select t from Tomadores t  inner join t.prestadores p "
						+ " where t.inscricaoTomador = '" + inscricaoTomador.trim() + "' "
						+ " and p.inscricaoPrestador = '" + inscricaoPrestador + "'");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return tomadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Tomadores save(Tomadores t) {

		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}

	public Tomadores update(Tomadores t) {

		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}


	public Tomadores findByInscricaoMunicipal(String inscMunicipal) {
		Query query = sessionFactory.openSession().createQuery(
				"from Tomadores t  " + " where t.inscricaoMunicipal like '%" + inscMunicipal.trim() + "%'");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return tomadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Tomadores> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Tomadores c where hash is null").setFirstResult(0).setMaxResults(320);
		List<Tomadores> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<Tomadores> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Tomadores set hash = '" + hash + "' where ");

		for (Tomadores c : listaAtualizados) {
			builder.append("id = " + c.getId() + " or ");
		}

		String sql = builder.toString();
		sql = sql.toString().substring(0, sql.length() - 4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public List<Tomadores> findAll() {
		Query query = sessionFactory.openSession().createQuery("from Tomadores t join fetch t.prestadores pr ");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return (List<Tomadores>) tomadores;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
