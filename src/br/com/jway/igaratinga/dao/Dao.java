package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.jway.igaratinga.util.HibernateUtil;


public class Dao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public Dao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void excluiDados(String nomeEntidade) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createQuery("delete from " + nomeEntidade + " ");
		query.executeUpdate();
		session.beginTransaction().commit();
		session.close();
	}
	
	public void ajustaEmailPrestadores() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("update prestadores A set A.email = (select B.email from notas_fiscais_prestadores B where B.inscricao_prestador=A.inscricao_prestador and B.email is not null order by B.numero_nota desc limit 1)");
		query.executeUpdate();
		session.beginTransaction().commit();
		session.close();
	}
	
	public void ajustaEmailPessoas() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("update pessoa A set A.email = (select B.email from notas_fiscais_prestadores B where B.inscricao_prestador=A.cnpj_cpf and B.email is not null order by B.numero_nota desc limit 1)");
		query.executeUpdate();
		session.beginTransaction().commit();
		session.close();
	}

	public Long count(String nomeEntidade) {
		Query query = sessionFactory.openSession()
				.createQuery("select count(*) from " + nomeEntidade );

		try {
			List<Long> qtdes = query.list();
			return qtdes.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	

	
	
}