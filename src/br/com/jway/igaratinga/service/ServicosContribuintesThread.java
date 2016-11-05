package br.com.jway.igaratinga.service;

import br.com.jway.igaratinga.dao.ServicosContribuintesDao;
import br.com.jway.igaratinga.entidadesOrigem.ServicosContribuinte;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class ServicosContribuintesThread implements Runnable{
	
	private String linha; 
	private Util util;
	
	private FileLog log;
	private ServicosContribuintesDao dao = new ServicosContribuintesDao();
	
	public ServicosContribuintesThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try{
			ServicosContribuinte s = new ServicosContribuinte(linha.substring(0,15), linha.substring(15,19), linha.substring(19));
			if (!dao.existe(s)){
				dao.save(s);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.fillError(linha, "Servicos Contribuintes ", e);
		}
		
	}

}
