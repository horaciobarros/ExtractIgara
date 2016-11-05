package br.com.jway.igaratinga.service;

import java.util.Map;

import br.com.jway.igaratinga.dao.AtividadeEconomicaDao;
import br.com.jway.igaratinga.dao.PrestadoresAtividadesDao;
import br.com.jway.igaratinga.dao.PrestadoresDao;
import br.com.jway.igaratinga.entidadesOrigem.AtividadeEconomicaContribuinte;
import br.com.jway.igaratinga.entidadesOrigem.Contribuinte;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class AtividadeEconomicaThread implements Runnable{
	
	private String linha; 
	private Util util;
	private FileLog log;
	private AtividadeEconomicaDao atividadeEconomicaDao = new AtividadeEconomicaDao();
	
	public AtividadeEconomicaThread(String linha, Util util,  FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		AtividadeEconomicaContribuinte a = new AtividadeEconomicaContribuinte(linha.substring(0, 15).trim(),
				linha.substring(15, 25).trim(), linha.substring(25, 275).trim(), linha.substring(275, 278).trim());

		try{
			if (!atividadeEconomicaDao.existe(a)){
				atividadeEconomicaDao.save(a);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.fillError(linha, "Atividade economica ", e);
		}
		
	}

}
