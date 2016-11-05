package br.com.jway.igaratinga.service;

import java.util.Map;

import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento52;
import br.com.jway.igaratinga.util.FileLog;

public class GuiasPagasThread implements Runnable{
	
	private String linha;
	private Map<String, GuiaRecolhimento52> mapGuiasPagas;
	private FileLog log;
	
	
	public GuiasPagasThread(String linha, FileLog log,  Map<String, GuiaRecolhimento52> mapGuiasPagas) {
		this.linha = linha;
		this.mapGuiasPagas = mapGuiasPagas;
		this.log = log;
	}

	@Override
	public void run() {
		GuiaRecolhimento52 g = new GuiaRecolhimento52(linha.substring(0, 10).trim(), linha.substring(10, 25).trim(),
				linha.substring(25, 35).trim(), linha.substring(35, 37).trim(),
				linha.substring(37, 101).trim().trim(), linha.substring(101, 111).trim(),
				linha.substring(111, 113).trim(), linha.substring(113, 117).trim(),
				linha.substring(117, 127).trim(), linha.substring(127, 137).trim(),
				linha.substring(137, 147).trim(), linha.substring(147, 157).trim(),
				linha.substring(157, 221).trim(), linha.substring(221, 477).trim());
		// System.out.println(g.getAnoCompetencia() + "-" +
		// g.getMesCompetencia() + "-"
		// + g.getInscricaoMunicipalContribuinte() + "-" + g.getValor() + "-
		// linhas lidas:" + count);
		mapGuiasPagas.put(g.getIdGuiaRecolhimento(), g);	
	}

}
