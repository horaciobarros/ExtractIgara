package br.com.jway.igaratinga.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.jway.igaratinga.model.MunicipiosIbge;
import br.com.jway.igaratinga.util.HibernateUtil;


public class MunicipiosIbgeDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public MunicipiosIbgeDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public String getCodigoIbge(String nomeMunicipio, String uf) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from MunicipiosIbge m  where upper(m.municipio) like :nomemun" + " and upper(m.uf) like :uf")
				.setParameter("nomemun", "%" + nomeMunicipio.trim().toUpperCase() + "%").setParameter("uf", "%" + uf.trim().toUpperCase() + "%");

		try {
			List<MunicipiosIbge> municipios = query.list();

			if (municipios.size() > 0) {
				return municipios.get(0).getCodigo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}

	public String findUfByCodigoIbge(String codigo) {
		Query query = sessionFactory.openSession().createQuery("from MunicipiosIbge m where m.codigo like :codigo").setParameter("codigo", codigo);

		try {
			List<MunicipiosIbge> municipios = query.list();

			if (municipios.size() > 0) {
				return municipios.get(0).getUf();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String findMunicipioByIbge(String municipioIbge) {
		Query query = sessionFactory.openSession().createQuery("from MunicipiosIbge m where m.codigo like :codigo").setParameter("codigo", municipioIbge);

		try {
			List<MunicipiosIbge> municipios = query.list();
			municipioIbge.length();
			if (municipios.size() > 0) {
				return municipios.get(0).getMunicipio();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
