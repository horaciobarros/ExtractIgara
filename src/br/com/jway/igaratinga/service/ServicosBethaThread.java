package br.com.jway.igaratinga.service;

import br.com.jway.igaratinga.dao.ServicosAliquotaDao;
import br.com.jway.igaratinga.dao.ServicosContribuintesDao;
import br.com.jway.igaratinga.dao.ServicosDao;
import br.com.jway.igaratinga.entidadesOrigem.Servicos;
import br.com.jway.igaratinga.entidadesOrigem.ServicosAliquota;
import br.com.jway.igaratinga.entidadesOrigem.ServicosContribuinte;
import br.com.jway.igaratinga.model.NotasFiscaisServicos;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class ServicosBethaThread implements Runnable{
	private Servicos serv;
	private Util util;
	private FileLog log;
	private ServicosDao dao = new ServicosDao();
	private ServicosAliquotaDao servicosAliquotaDao = new ServicosAliquotaDao();

	public ServicosBethaThread(Servicos serv, Util util, FileLog log) {
		this.serv = serv;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try{
			ServicosAliquota servAliquota = servicosAliquotaDao.findByCodigo(serv.getCodigo());
			if (servAliquota!=null){
				serv.setAliquota(servAliquota.getAliquota());
				dao.update(serv);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			log.fillError(serv.getId()+"", "Servicos Betha ", e);
		}
	}

}
