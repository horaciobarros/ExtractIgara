package br.com.jway.igaratinga.service;

import java.math.BigDecimal;
import java.util.Map;

import br.com.jway.igaratinga.dao.CompetenciasDao;
import br.com.jway.igaratinga.dao.GuiasDao;
import br.com.jway.igaratinga.dao.PagamentosDao;
import br.com.jway.igaratinga.dao.PrestadoresDao;
import br.com.jway.igaratinga.entidadesOrigem.Contribuinte;
import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento51;
import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento52;
import br.com.jway.igaratinga.model.Competencias;
import br.com.jway.igaratinga.model.Guias;
import br.com.jway.igaratinga.model.Pagamentos;
import br.com.jway.igaratinga.model.Prestadores;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class GuiasGeradasThread implements Runnable {

	private String linha;
	private Map<String, GuiaRecolhimento52> mapGuiasPagas;
	private FileLog log;
	private CompetenciasDao competenciasDao = new CompetenciasDao();
	private Util util;
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private GuiasDao guiasDao = new GuiasDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	private Map<String, Contribuinte> mapContribuinte;

	public GuiasGeradasThread(String linha, Util util, FileLog log, Map<String, GuiaRecolhimento52> mapGuiasPagas,
			Map<String, Contribuinte> mapContribuinte) {
		this.linha = linha;
		this.mapGuiasPagas = mapGuiasPagas;
		this.log = log;
		this.util = util;
		this.mapContribuinte = mapContribuinte;
	}

	@Override
	public void run() {
		GuiaRecolhimento51 g = new GuiaRecolhimento51(linha.substring(0, 10).trim(), linha.substring(10, 25).trim(),
				linha.substring(25, 35).trim(), linha.substring(35, 37).trim(), linha.substring(37, 101).trim(),
				linha.substring(101, 111).trim(), linha.substring(111, 121).trim(), linha.substring(121, 131).trim(),
				linha.substring(131, 141).trim(), linha.substring(141, 143).trim(), linha.substring(143, 147).trim());

		// System.out.println(g.getAnoCompetencia() + "-" +
		// g.getMesCompetencia() + "-"
		// + g.getInscricaoMunicipalContribuinte() + "-" + g.getValor() + "-
		// linhas lidas:" + count);

		String descricao = util.getNomeMes(g.getMesCompetencia().trim()) + "/" + g.getAnoCompetencia();
		Competencias cp = competenciasDao.findByDescricao(descricao);

		try {

			Prestadores pr = new Prestadores();
			pr = prestadoresDao.findByInscricaoMunicipal(g.getInscricaoMunicipalContribuinte().trim());
			Guias guias = new Guias();
			guias.setCompetencias(cp);
			guias.setDataVencimento(util.getStringToDate(g.getDataVencimento()));
			String cnpj = mapContribuinte.get(g.getInscricaoMunicipalContribuinte().trim()).getCnpjCpf();
			guias.setInscricaoPrestador(cnpj);
			guias.setIntegrarGuia("N");
			guias.setNumeroGuia(Long.valueOf(g.getNumGuia().trim()));
			guias.setPrestadores(prestadoresDao.findByInscricao(cnpj));
			guias.setSituacao(getSituacaoGuia(g));
			guias.setValorGuia(util.getStringToBigDecimal(g.getValor()));
			guias.setTipo("P");
			guias.setValorImposto(guias.getValorGuia());
			guias.setIdGuiaRecolhimento(g.getIdGuiaRecolhimento());
			guiasDao.save(guias);
			// System.out.println(guias.getNumeroGuia() + " - " +
			// guias.getInscricaoPrestador());

			if (getSituacaoGuia(g).equals("P")) {
				GuiaRecolhimento52 guiaPaga = mapGuiasPagas.get(g.getIdGuiaRecolhimento());
				Pagamentos pg = new Pagamentos();
				try {
					if (!util.isEmptyOrNull(guiaPaga.getDataPagamento().trim())) {
						pg.setDataPagamento(util.getStringToDate(guiaPaga.getDataPagamento().trim()));
					} else {
						pg.setDataPagamento(util.getStringToDate(guiaPaga.getDataVencimento().trim()));
					}
					pg.setGuias(guias);
					pg.setNumeroGuia(guias.getNumeroGuia());
					pg.setNumeroPagamento(Long.valueOf(guiaPaga.getParcela().trim()));
					pg.setTipoPagamento("N");
					pg.setValorPago(util.getStringToBigDecimal(guiaPaga.getValorRecebido()));
					pg.setValorCorrecao(BigDecimal.valueOf(0));
					pg.setValorDiferenca(BigDecimal.valueOf(0));
					pg.setValorJuro(BigDecimal.valueOf(0));
					pg.setValorMulta(BigDecimal.valueOf(0));
					pagamentosDao.save(pg);
				} catch (Exception e) {
					System.out.println(" Pagamento de guia não gravado: " + guiaPaga.getDataPagamento());
					log.fillError(linha, "Guia  ", e);
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" Guia não gravada: " + g.getNumGuia());
			log.fillError(linha, "Guia  ", e);
		}

	}

	private String getSituacaoGuia(GuiaRecolhimento51 g) {
		GuiaRecolhimento52 guiaPaga = mapGuiasPagas.get(g.getIdGuiaRecolhimento());
		if (guiaPaga != null) {
			return "P";
		} else {
			return "A";
		}
	}

}
