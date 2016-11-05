package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.jway.igaratinga.model.Pessoa;
import br.com.jway.igaratinga.util.HibernateUtil;

public class PessoaDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	
	public PessoaDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Pessoa findByCnpjCpf(String cnpjCpf) {
		Query query = sessionFactory.openSession()
				.createQuery("from Pessoa p  " + " where p.cnpjCpf like '%" + cnpjCpf.trim() + "%'");

		try {
			List<Pessoa> pessoa = query.list();

			if (pessoa.size() > 0) {
				return pessoa.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Pessoa save(Pessoa p) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return p;
	}
	
	public Pessoa update(Pessoa p) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(p);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return p;
	}
	
	
	public List<Pessoa> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from Pessoa p where hash is null").setFirstResult(0).setMaxResults(200);
		List<Pessoa> lista = query.list();
		tx.commit();session.close();

		return lista;
	}

	public void saveHash(List<Pessoa> listaAtualizados, String hash){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Pessoa set hash = '"+hash+"' where ");
		
		for (Pessoa c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
	}
	
	public List<Pessoa> findAll() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from Pessoa p ");
		List<Pessoa> lista = query.list();
		tx.commit();session.close();

		return lista;
	}
	
	public Pessoa ultimoPessoaIdGravado() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from Pessoa p order by pessoaId desc").setFirstResult(0).setMaxResults(1);
		List<Pessoa> lista = query.list();
		tx.commit();session.close();

		return lista.get(0);
	}
	
	public void excluiPrestadoresSemNotas() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("delete from pessoa where "
				+ "cnpj_cpf not in (select n.inscricao_prestador from notas_fiscais n) and "
				+ "cnpj_cpf not in(select g.inscricao_prestador from guias g); ");

		String sql = builder.toString();
		Query query = session.createSQLQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
		
	}

}
