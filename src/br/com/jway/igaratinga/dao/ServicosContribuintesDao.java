package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.jway.igaratinga.entidadesOrigem.ServicosContribuinte;
import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.util.HibernateUtil;

public class ServicosContribuintesDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	
	public ServicosContribuintesDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public boolean existe(ServicosContribuinte a) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from ServicosContribuinte a where a.id ="+a.getId());
		List<NotasFiscais> lista = query.list();
		tx.commit();
		session.close();
		if (lista!=null && lista.size() > 0) {
			return true;
		}
		else{
			return false;
		}
	}

	public void save(ServicosContribuinte a) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(a);
		session.beginTransaction().commit();
		session.close();	
	}

}
