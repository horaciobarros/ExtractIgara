package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.jway.igaratinga.entidadesOrigem.ServicosAliquota;
import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.util.HibernateUtil;

public class ServicosAliquotaDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	
	public ServicosAliquotaDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public ServicosAliquota findByCodigo(String codigo) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from ServicosAliquota s where s.codigo like '%"+codigo.trim()+"%'");
		List<ServicosAliquota> lista = query.list();
		tx.commit();
		session.close();
		if (lista != null && lista.size()>0){
			return lista.get(0);
		} else{
			return null;
		}
	}

}
