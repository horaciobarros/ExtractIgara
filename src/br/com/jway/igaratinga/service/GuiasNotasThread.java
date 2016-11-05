package br.com.jway.igaratinga.service;

import br.com.jway.igaratinga.dao.GuiasDao;
import br.com.jway.igaratinga.dao.GuiasNotasFiscaisDao;
import br.com.jway.igaratinga.dao.NotasFiscaisDao;
import br.com.jway.igaratinga.entidadesOrigem.GuiasNFSe;
import br.com.jway.igaratinga.model.Guias;
import br.com.jway.igaratinga.model.GuiasNotasFiscais;
import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class GuiasNotasThread implements Runnable{
	
	private String linha;
	private Util util;
	private FileLog log;
	private GuiasDao guiasDao = new GuiasDao();
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();
	
	public GuiasNotasThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try {

			GuiasNFSe guiaOrigem = new GuiasNFSe(linha.substring(0, 2).trim(), linha.substring(2, 7).trim(),
					linha.substring(7, 22).trim(), linha.substring(22, 37).trim(), linha.substring(37, 46).trim(),
					linha.substring(46, 54).trim(), linha.substring(54).trim());

			Guias g = guiasDao.findByIdGuiaRecolhimento(guiaOrigem.getIdGuiaRecolhimento());

			if (g != null) {
				NotasFiscais nf = notasFiscaisDao.findByNumeroVerificacaoInscricaoPrestador(guiaOrigem.getCodigoVerificacao(), g.getInscricaoPrestador());

				if (nf != null) {
					
					if (!nf.getInscricaoPrestador().equals(g.getInscricaoPrestador())) {
						throw new Exception("Dados inconsistentes (inscricao prestador)");
					}
					
					if (nf.getOptanteSimples().equals("S") || !nf.getSituacaoTributaria().equals("N") || !nf.getSituacaoOriginal().equals("N")){
						return;
					}
					GuiasNotasFiscais gnf = new GuiasNotasFiscais();

					gnf.setGuias(g);
					gnf.setInscricaoPrestador(g.getInscricaoPrestador()); //
					gnf.setNumeroGuia(g.getNumeroGuia());
					gnf.setNumeroNota(nf.getNumeroNota());
					gnf.setSituacaoTributaria(nf.getSituacaoTributaria());
					gnf.setNotasFiscais(nf);
					guiasNotasFiscaisDao.save(gnf);
					nf.setSituacao("E");
					nf = notasFiscaisDao.update(nf);
				}
			}

		} catch (Exception e) {
			log.fillError(linha, "Guia X nota não gravada", e);
			e.printStackTrace();
		}
	}

}
