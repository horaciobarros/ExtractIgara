package br.com.jway.igaratinga.service;

import br.com.jway.igaratinga.dao.NotasFiscaisDao;
import br.com.jway.igaratinga.dao.NotasFiscaisSubstDao;
import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.model.NotasFiscaisSubst;
import br.com.jway.igaratinga.util.FileLog;

public class NotasSubstituidasThread implements Runnable {
	
	private NotasFiscais nfSubstituida;
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private NotasFiscaisSubst nfsub;
	private NotasFiscaisSubstDao notasFiscaisSubstDao = new NotasFiscaisSubstDao();
	private NotasFiscais nf;
	private FileLog log;
	
	public NotasSubstituidasThread(NotasFiscais nfSubstituida, FileLog log) {
		this.nfSubstituida = nfSubstituida;
		this.log = log;
	}

	@Override
	public void run() {
		// buscando a nota substituta
		nf = notasFiscaisDao.findByNumeroDocumentoInscricaoPrestador(nfSubstituida.getNumeroNotaFiscalSubstituta(), 
				nfSubstituida.getInscricaoPrestador()); 
		if (nf != null) {
			try {
				nfsub = new NotasFiscaisSubst();
				nfsub.setDatahorasubstituicao(nfSubstituida.getDataHoraEmissao());
				nfsub.setInscricaoPrestador(nfSubstituida.getInscricaoPrestador());
				nfsub.setNumeroNota(Long.valueOf(nfSubstituida.getNumeroNota()));
				nfsub.setNumeroNotaSubstituta(nf.getNumeroNota());
				nfsub.setMotivo("Dados incorretos");
				nfsub.setNotasFiscais(nf);
				notasFiscaisSubstDao.save(nfsub);
				nfsub = null;
			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(nfSubstituida.getNumeroNota().toString() + nfSubstituida.getNomePrestador(), "Nota Fiscal Substituta", e);
			}
			nfSubstituida = null;
		} else {
			System.out.println("Nota Fiscal substituta não encontrada:" + nf.getNumeroNotaFiscalSubstituta());
			log.fillError("Nota Fiscal substituta não encontrada:", nf.getNumeroNotaFiscalSubstituta());
		}

	}

}
