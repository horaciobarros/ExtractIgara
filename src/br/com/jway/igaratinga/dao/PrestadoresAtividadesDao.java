package br.com.jway.igaratinga.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.jway.igaratinga.model.PrestadoresAtividades;
import br.com.jway.igaratinga.util.HibernateUtil;

public class PrestadoresAtividadesDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public PrestadoresAtividadesDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void save(PrestadoresAtividades pa) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(pa);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<PrestadoresAtividades> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from PrestadoresAtividades pa where hash is null").setFirstResult(0).setMaxResults(1000);
		List<PrestadoresAtividades> lista = query.list();
		tx.commit();session.close();

		return lista;
	}
	
	public void saveHash(List<PrestadoresAtividades> listaAtualizados, String hash){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update PrestadoresAtividades set hash = '"+hash+"' where ");
		
		for (PrestadoresAtividades pa : listaAtualizados){
			builder.append("id = "+pa.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
	}
	
	public PrestadoresAtividades findByInscricao(String inscricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from PrestadoresAtividades pa  " + " where pa.inscricaoPrestador like '%" + inscricao.trim() + "%'");

		
		try {
			List<PrestadoresAtividades> prestadoresAtividadesList = query.list();

			if (prestadoresAtividadesList.size() > 0) {
				return prestadoresAtividadesList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	public PrestadoresAtividades findByInscricaoItemListaServico(String inscricao, String itemListaServico) {
		Query query = sessionFactory.openSession()
				.createQuery("from PrestadoresAtividades pa  " + " where pa.inscricaoPrestador like '%" + inscricao.trim() + "%'" + 
		" and pa.iListaServicos = '" + itemListaServico.trim() + "'");

		try {
			List<PrestadoresAtividades> prestadoresAtividadesList = query.list();

			if (prestadoresAtividadesList.size() > 0) {
				return prestadoresAtividadesList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

	public void excluiPrestadoresSemNotas() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("delete from prestadores_atividades where "
				+ "inscricao_prestador not in (select n.inscricao_prestador from notas_fiscais n) and "
				+ "inscricao_prestador not in(select g.inscricao_prestador from guias g)");

		String sql = builder.toString();
		Query query = session.createSQLQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
		
	}

	public PrestadoresAtividades findByInscricaoItemListaServicoCnae(String inscricaoPrestador, String itemListaServico, String icnaes) {
		List<PrestadoresAtividades> prestadoresAtividadesList = new ArrayList<PrestadoresAtividades>();
		if (icnaes == null || icnaes.trim().isEmpty()){
			Query query = sessionFactory.openSession()
					.createQuery("from PrestadoresAtividades pa  " + " where pa.inscricaoPrestador like '%" + inscricaoPrestador.trim() + "%'" + 
			" and pa.iListaServicos = '" + itemListaServico.trim()+"'");
			prestadoresAtividadesList = query.list();
		}
		else{
			Query query = sessionFactory.openSession()
					.createQuery("from PrestadoresAtividades pa  " + " where pa.inscricaoPrestador like '%" + inscricaoPrestador.trim() + "%'" + 
			" and pa.iListaServicos = '" + itemListaServico.trim() + "' and pa.icnaes = :cnae").setParameter("cnae",  icnaes);
			prestadoresAtividadesList = query.list();
		}

		try {
			

			if (prestadoresAtividadesList.size() > 0) {
				return prestadoresAtividadesList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

}
