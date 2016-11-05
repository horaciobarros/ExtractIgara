package br.com.jway.igaratinga.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.jway.igaratinga.dao.CompetenciasDao;
import br.com.jway.igaratinga.dao.Dao;
import br.com.jway.igaratinga.dao.GuiasDao;
import br.com.jway.igaratinga.dao.GuiasNotasFiscaisDao;
import br.com.jway.igaratinga.dao.NotasFiscaisDao;
import br.com.jway.igaratinga.dao.NotasFiscaisServicosDao;
import br.com.jway.igaratinga.dao.PessoaDao;
import br.com.jway.igaratinga.dao.PrestadoresAtividadesDao;
import br.com.jway.igaratinga.dao.PrestadoresDao;
import br.com.jway.igaratinga.dao.PrestadoresOptanteSimplesDao;
import br.com.jway.igaratinga.dao.ServicosDao;
import br.com.jway.igaratinga.dao.TomadoresDao;
import br.com.jway.igaratinga.entidadesOrigem.Contribuinte;
import br.com.jway.igaratinga.entidadesOrigem.DadosLivroPrestador;
import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento51Da;
import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento52;
import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento53;
import br.com.jway.igaratinga.entidadesOrigem.GuiaRecolhimento54;
import br.com.jway.igaratinga.entidadesOrigem.Servicos;
import br.com.jway.igaratinga.model.Competencias;
import br.com.jway.igaratinga.model.Guias;
import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.model.NotasFiscaisServicos;
import br.com.jway.igaratinga.model.Tomadores;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class ExtractorService {

	Map<String, Contribuinte> mapContribuinte = new Hashtable<String, Contribuinte>();

	private Util util = new Util();
	private CompetenciasDao competenciasDao = new CompetenciasDao();
	private Dao dao = new Dao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private PessoaDao pessoaDao = new PessoaDao();
	private GuiasDao guiasDao = new GuiasDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private NotasFiscais notasFiscais = new NotasFiscais();

	private Map<String, GuiaRecolhimento52> mapGuiasPagas = new Hashtable<String, GuiaRecolhimento52>();

	public List<String> excluiParaProcessarNivel1() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades",
				"" + "PrestadoresOptanteSimples", "Guias", "Competencias", "NotasFiscais", "Tomadores", "Prestadores",
				"Pessoa");

	}

	public List<String> excluiParaProcessarNivel2() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "Pagamentos", "PrestadoresAtividades",
				"PrestadoresOptanteSimples", "Guias", "NotasFiscais", "Tomadores");

	}

	public List<String> excluiParaProcessarNivel3() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisCanceladas", "NotasFiscaisCondPagamentos",
				"NotasFiscaisEmails", "NotasFiscaisObras", "NotasFiscaisPrestadores", "NotasFiscaisServicos",
				"NotasFiscaisSubst", "NotasFiscaisTomadores", "NotasFiscaisXml", "NotasFiscais", "Tomadores", "PrestadoresAtividades");
	}

	public List<String> excluiParaProcessarNivel4() {
		return Arrays.asList("GuiasNotasFiscais", "NotasFiscaisSubst");
	}

	public void excluiDados(String nomeEntidade) {

		dao.excluiDados(nomeEntidade);
	}

	public List<String> lerArquivo(String arquivoIn) {
		BufferedReader br;
		List<String> dadosList = new ArrayList<String>();
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream("c:/TEMP/igaratinga/" + arquivoIn + ".txt"), "cp1252"));

			while (br.ready()) {
				String linha = br.readLine();
				dadosList.add(linha);
			}
			br.close();
			return dadosList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void incluiCompetencias() {

		for (int ano = 2010; ano < 2017; ano++) {

			for (int mes = 1; mes <= 12; mes++) {
				String descricao = util.getNomeMes(Integer.toString(mes)) + "/" + ano;
				Competencias cp = competenciasDao.findByDescricao(descricao);

				try {
					if (cp == null || cp.getId() == 0) { // acertar datas
						cp = new Competencias();
						cp.setDescricao(descricao.trim());
						cp.setDataInicio(util.getFirstDayOfMonth(Integer.toString(ano), Integer.toString(mes)));
						cp.setDataFim(util.getLastDayOfMonth(Integer.toString(ano), Integer.toString(mes)));
						cp.setDataVencimento(util.getVencimentoCompetencia(cp.getDataFim()));

						competenciasDao.save(cp);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void processaDadosNotasFiscais() {

		FileLog log = new FileLog("notas_fiscais");
		String pasta = "c:\\temp\\igaratinga\\XMLs\\";
		File dir = new File(pasta);
		File[] arquivos = dir.listFiles();
		ExecutorService executorLeitura = Executors.newFixedThreadPool(100);
		ExecutorService executorGravacao = Executors.newFixedThreadPool(400);
		for (File f : arquivos) {
			try {
				LeituraXmlThreads thread = new LeituraXmlThreads(f, executorGravacao, log, util);
				executorLeitura.execute(thread);
			} catch (Exception e) {
				log.fillError(f.getName(), "Erro na leitura" + e.getMessage());
				e.printStackTrace();
			}
		}
		executorLeitura.shutdown();
		while (!executorLeitura.isTerminated()) {
		}
		System.out.println("Fim de leitura " + Util.getDataHoraAtual());
		Util.pausar(5000);
		executorGravacao.shutdown();
		while (!executorGravacao.isTerminated()) {
		}
		log.close();
	}

	public void processaDadosContribuinte(List<String> dadosList) {
		FileLog log = new FileLog("contribuinte");
		int count = 0;

		ExecutorService executor = Executors.newFixedThreadPool(200);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				break;
			}
			count++;
			ContribuinteThread thread = new ContribuinteThread(linha, util, count, mapContribuinte, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public void processaDadosAtividadeEconomicaContribuinte(List<String> dadosList) {
		FileLog log = new FileLog("atividade_economica");
		ExecutorService executor = Executors.newFixedThreadPool(200);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}
			AtividadeEconomicaThread thread = new AtividadeEconomicaThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}
	

	public void processaDadosServicosAutorizadosContribuinte(List<String> dadosList) {
		FileLog log = new FileLog("serv_autorizados");
		ExecutorService executor = Executors.newFixedThreadPool(200);
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}
			ServicosContribuintesThread thread = new ServicosContribuintesThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
		
	}
	
	public void processaDadosServicosBetha() {
		FileLog log = new FileLog("servicos_betha");
		ExecutorService executor = Executors.newFixedThreadPool(200);
		ServicosDao dao = new ServicosDao();
		for (Servicos serv : dao.findAll()) {

			ServicosBethaThread thread = new ServicosBethaThread(serv, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
		
	}

	public void processaDadosGuiasPagas(List<String> dadosList) {
		FileLog log = new FileLog("guias_pagas");
		ExecutorService executor = Executors.newFixedThreadPool(200);
		mapGuiasPagas = new Hashtable<String, GuiaRecolhimento52>();
		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}
			GuiasPagasThread thread = new GuiasPagasThread(linha, log, mapGuiasPagas);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public void processaDadosGuiasGeradas(List<String> dadosList) {
		FileLog log = new FileLog("guias_geradas");
		ExecutorService executor = Executors.newFixedThreadPool(200);

		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}
			GuiasGeradasThread thread = new GuiasGeradasThread(linha, util, log, mapGuiasPagas, mapContribuinte);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public void processaDadosGuiasDA(List<String> dadosList) {

		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}

			GuiaRecolhimento51Da g = new GuiaRecolhimento51Da(linha.substring(0, 10), linha.substring(10, 25),
					linha.substring(25, 35), linha.substring(35, 37), linha.substring(37, 101),
					linha.substring(101, 111), linha.substring(111, 121), linha.substring(121, 131),
					linha.substring(131, 141), linha.substring(141, 143), linha.substring(143, 147));

			// System.out.println(g.getAnoCompetencia() + "-" +
			// g.getMesCompetencia() + "-"
			// + g.getInscricaoMunicipalContribuinte() + "-" + g.getValor() + "-
			// linhas lidas:" + count);

		}

	}

	public void processaDadosGuiaSuspensas(List<String> dadosList) {

		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}

			GuiaRecolhimento53 g = new GuiaRecolhimento53(linha.substring(0, 10), linha.substring(10, 25),
					linha.substring(25, 35), linha.substring(35, 37), linha.substring(37, 101),
					linha.substring(101, 111), linha.substring(111, 121), linha.substring(121, 131),
					linha.substring(131, 141), linha.substring(141, 143), linha.substring(143, 147),
					linha.substring(147, 157), linha.substring(157, 413), linha.substring(413, 541));
			// System.out.println(g.getAnoCompetencia() + "-" +
			// g.getMesCompetencia() + "-"
			// + g.getInscricaoMunicipalContribuinte() + "-" + g.getValor() + "-
			// linhas lidas:" + count);

		}

	}

	public void processaDadosGuiaCanceladas(List<String> dadosList) {

		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}

			if (linha.substring(0, 30).trim().isEmpty()) {
				continue;
			}

			try {
				GuiaRecolhimento54 g = new GuiaRecolhimento54(linha.substring(0, 10), linha.substring(10, 25),
						linha.substring(25, 35), linha.substring(35, 37), linha.substring(37, 101),
						linha.substring(101, 111), linha.substring(111, 121), linha.substring(121, 131),
						linha.substring(131, 141), linha.substring(141, 143), linha.substring(143, 147),
						linha.substring(147, 157), linha.substring(157, 413), linha.substring(413, 541));

				// System.out.println(g.getAnoCompetencia() + "-" +
				// g.getMesCompetencia() + "-"
				// + g.getInscricaoMunicipalContribuinte() + "-" + g.getValor()
				// + "- linhas lidas:" + count);
			} catch (Exception e) {

			}

		}

	}

	public void processaAssociacaoGuiasNotasFiscais(List<String> dadosList) {

		FileLog log = new FileLog("guias_notas");
		ExecutorService executor = Executors.newFixedThreadPool(100);

		for (String linha : dadosList) {
			if (linha == null || linha.trim().isEmpty()) {
				continue;
			}
			GuiasNotasThread thread = new GuiasNotasThread(linha, util, log);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		log.close();
	}

	public Long count(String nomeEntidade) {
		return dao.count(nomeEntidade);
	}

	public void processaExclusaoPrestadoresSemNotas() {
		System.out.println("Excluindo Prestadores Atividades");
		new PrestadoresAtividadesDao().excluiPrestadoresSemNotas();
		System.out.println("Excluindo Prestadores optante simples");
		new PrestadoresOptanteSimplesDao().excluiPrestadoresSemNotas();
		System.out.println("Excluindo Prestadores");
		prestadoresDao.excluiPrestadoresSemNotas();
		System.out.println("Excluindo Pessoas");
		pessoaDao.excluiPrestadoresSemNotas();
	}

	public void excluiGuiasSemNotas() {

		GuiasDao guiasDao = new GuiasDao();
		Util.pausar(3000);

		System.out.println("Excluindo guias retidas");
		GuiasNotasFiscaisDao dao = new GuiasNotasFiscaisDao();
		dao.deleteGuiasRetidas();

		Util.pausar(3000);
		System.out.println("Excluindo guias sem notas");
		ExecutorService executor = Executors.newFixedThreadPool(200);
		for (Guias guias : guiasDao.findAll()) {
			ExcluirGuiasThread thread = new ExcluirGuiasThread(guias);
			executor.execute(thread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Guias excluidas");
	}

	public void processaUnificacaoTomadores() {

		try {
			Map<String, Tomadores> tomadoresMap = new HashMap<String, Tomadores>();

			String key;
			Tomadores tAux = new Tomadores();
			for (Tomadores t : tomadoresDao.findAll()) {
				key = t.getInscricaoTomador().trim() + "-" + t.getPrestadores().getInscricaoPrestador().trim();
				if (!tomadoresMap.containsKey(key)) {
					tomadoresMap.put(key, t);
				} else {
					tAux = tomadoresMap.get(key);
					if (t.getDataAtualizacao().getTime() > tAux.getDataAtualizacao().getTime()) {
						tomadoresMap.put(key, t);
					}
				}
			}

			excluiDados("Tomadores");

			for (Tomadores t : tomadoresMap.values()) {
				t = tomadoresDao.save(t);
			}

			System.out.println("Verificando integridade de notas ");

			for (NotasFiscais nf : notasFiscaisDao.findAll()) {
				if (!util.isEmptyOrNull(nf.getInscricaoTomador())) {
					Tomadores t = tomadoresDao.findByInscricao(nf.getInscricaoTomador(), nf.getInscricaoPrestador());
					if (t == null || t.getId() == null) {
						try {
							throw new Exception("Tomador " + nf.getInscricaoTomador() + " Prestador " + nf.getInscricaoPrestador() + " da nota " + nf.getNumeroNota()
									+ " não encontrado.");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void processaDadosNotasFiscaisSubstituidas() {

		FileLog log = new FileLog("notas_fiscais_substituidas");

		ExecutorService executor = Executors.newFixedThreadPool(300);
		for (NotasFiscais nf : notasFiscaisDao.findSubstituidas()) {
			NotasSubstituidasThread thread = new NotasSubstituidasThread(nf, log);
			executor.execute(thread);
		}
		Util.pausar(5000);
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Notas Fiscais finalizada - " + Util.getDataHoraAtual());

	}

	public void processaAjusteEmails() {
		System.out.println("Email Prestadores " + Util.getDataHoraAtual());
		dao.ajustaEmailPrestadores();
		System.out.println("Email PEssoas " + Util.getDataHoraAtual());
		dao.ajustaEmailPessoas();
	}
	

}