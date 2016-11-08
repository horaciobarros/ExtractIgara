package br.com.igaratinga.controller;

import java.util.List;

import br.com.jway.igaratinga.service.ExtractorService;
import br.com.jway.igaratinga.util.Util;

/**
 * 
 * @author jway
 *
 */
public class Controller {

	private ExtractorService extractorService = new ExtractorService();

	public void importaNfe() {

		int nivelProcessamento = 1;

		// limpando o banco
		System.out.println("Limpando o banco de Igaratinga nível " + nivelProcessamento);

		List<String> entidades = null;
		if (nivelProcessamento == 2) {
			entidades = extractorService.excluiParaProcessarNivel2();
		} else if (nivelProcessamento == 3) {
			entidades = extractorService.excluiParaProcessarNivel3();
		} else if (nivelProcessamento == 1) {
			entidades = extractorService.excluiParaProcessarNivel1();
		} else if (nivelProcessamento == 4) {
			entidades = extractorService.excluiParaProcessarNivel4();
		}

		for (String nomeEntidade : entidades) {
			try {
				extractorService.excluiDados(nomeEntidade);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Inï¿½cio
		System.out.println("Importação de dados de arquivos txt de Igaratinga - Início - " + nivelProcessamento);
		List<String> dadosList;

		if (nivelProcessamento == 1) {
			System.out.println("Lendo contribuintes " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("contribuinte");
			System.out.println("Gravando contribuintes " + Util.getDataHoraAtual());
			extractorService.processaDadosContribuinte(dadosList);
			System.out.println("--- Fim de contribuintes --- " + Util.getDataHoraAtual());

			System.out.println("Gravando competencias " + Util.getDataHoraAtual());
			extractorService.incluiCompetencias();
			
			System.out.println("Lendo atividade de contribuintes " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("AtvEconomica");
			System.out.println("Gravando atividades " + Util.getDataHoraAtual());
			extractorService.processaDadosAtividadeEconomicaContribuinte(dadosList);
			
			System.out.println("Lendo servicos de contribuintes " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("ServAutorizados");
			System.out.println("Gravando servicos " + Util.getDataHoraAtual());
			extractorService.processaDadosServicosAutorizadosContribuinte(dadosList);
		}

		if (nivelProcessamento <= 2) {
			System.out.println("Lendo guias pagas " + Util.getDataHoraAtual()); 
			dadosList = extractorService.lerArquivo("GuiasPagas");

			extractorService.processaDadosGuiasPagas(dadosList);
			System.out.println("--- Fim de guias paga --- " + Util.getDataHoraAtual());

			System.out.println("Lendo guias geradas " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("GuiasGeradas");
			System.out.println("Gravando guias " + Util.getDataHoraAtual());
			extractorService.processaDadosGuiasGeradas(dadosList);
			System.out.println("--- Fim de guias geradas --- " + Util.getDataHoraAtual());

			System.out.println("Lendo guias DA " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("GuiasDA");
			extractorService.processaDadosGuiasDA(dadosList);
			System.out.println("--- Fim de guias DA --- " + Util.getDataHoraAtual());

			System.out.println("Lendo guias suspensas " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("GuiasSuspensas");
			extractorService.processaDadosGuiaSuspensas(dadosList);
			System.out.println("--- Fim de guias suspensas --- " + Util.getDataHoraAtual());

			System.out.println("Lendo guias canceladas " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("GuiasCanceladas");
			extractorService.processaDadosGuiaCanceladas(dadosList);
			System.out.println("--- Fim de guias canceladas --- " + Util.getDataHoraAtual());
		}

		if (nivelProcessamento <= 3) {

			System.out.println("Lendo e Gravando Notas Fiscais " + Util.getDataHoraAtual());
			extractorService.processaDadosNotasFiscais();
			System.out.println("--- Fim de Notas Fiscais --- " + Util.getDataHoraAtual());
		}
		
		if (nivelProcessamento <= 4) {
			System.out.println("Gravando substituição de notas " + Util.getDataHoraAtual());
			extractorService.processaDadosNotasFiscaisSubstituidas();
			System.out.println("--- Fim gravação de substituição de notas --- " + Util.getDataHoraAtual());

			System.out.println("Lendo Associação Guias x Notas Fiscais " + Util.getDataHoraAtual());
			dadosList = extractorService.lerArquivo("GuiasNFS-e");
			extractorService.processaAssociacaoGuiasNotasFiscais(dadosList);
			System.out.println("--- Fim de Associação Guias x Notas Fiscais --- " + Util.getDataHoraAtual());
			
			
		}
		
		if (nivelProcessamento <= 5) {

			System.out.println("Iniciando processo de excluir guias sem notas... ");
			extractorService.excluiGuiasSemNotas();

			System.out.println("Limpando Prestadores Sem Notas");
			extractorService.processaExclusaoPrestadoresSemNotas();
			
			System.out.println("Unifica tomadores");
			extractorService.processaUnificacaoTomadores();

			System.out.println("Processando ajuste em tabela de servicos Betha " + Util.getDataHoraAtual());
			extractorService.processaDadosServicosBetha();
			
			System.out.println("Processando ajuste em emails de Pessoas e Prestadores" + Util.getDataHoraAtual());
			extractorService.processaAjusteEmails();
		}

		
		System.out.println("--- Processo encerrado. " + Util.getDataHoraAtual() + " Registros gravados: ");

		for (String nomeEntidade : entidades) {
			try {
				System.out.println(nomeEntidade + " -->" + extractorService.count(nomeEntidade));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//Util.desligarComputador();
	}

}
