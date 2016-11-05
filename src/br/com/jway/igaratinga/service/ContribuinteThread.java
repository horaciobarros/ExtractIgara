package br.com.jway.igaratinga.service;

import java.util.Map;

import br.com.jway.igaratinga.dao.MunicipiosIbgeDao;
import br.com.jway.igaratinga.dao.PessoaDao;
import br.com.jway.igaratinga.dao.PrestadoresDao;
import br.com.jway.igaratinga.dao.PrestadoresOptanteSimplesDao;
import br.com.jway.igaratinga.entidadesOrigem.Contribuinte;
import br.com.jway.igaratinga.model.Pessoa;
import br.com.jway.igaratinga.model.Prestadores;
import br.com.jway.igaratinga.model.PrestadoresOptanteSimples;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class ContribuinteThread implements Runnable {

	private String linha;
	private Util util;
	private int count;
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();
	private PessoaDao pessoaDao = new PessoaDao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private Map<String, Contribuinte> mapContribuinte;
	private FileLog log;
	private PrestadoresOptanteSimplesDao prestadoresOptanteSimplesDao = new PrestadoresOptanteSimplesDao();

	public ContribuinteThread(String linha, Util util, int count, Map<String, Contribuinte> mapContribuinte,
			FileLog log) {
		this.linha = linha;
		this.util = util;
		this.count = count;
		this.mapContribuinte = mapContribuinte;
		this.log = log;
	}

	@Override
	public void run() {
		Contribuinte c = new Contribuinte(linha.substring(0, 15).trim(), linha.substring(15, 16).trim(),
				linha.substring(16, 30).trim(), linha.substring(30, 158).trim(), linha.substring(158, 228).trim(),
				linha.substring(228, 248).trim(), linha.substring(248, 258).trim(), linha.substring(258, 268).trim(),
				linha.substring(268, 300).trim(), linha.substring(300, 320).trim(), linha.substring(320, 384).trim(),
				linha.substring(384, 448).trim(), linha.substring(448, 512).trim(), linha.substring(512, 522).trim(),
				linha.substring(522, 532).trim(), linha.substring(532, 542).trim(), linha.substring(542, 552).trim(),
				linha.substring(552, 562).trim(), linha.substring(562, 565).trim(), linha.substring(565, 568).trim(),
				linha.substring(568, 571).trim(), linha.substring(571, 574).trim(), linha.substring(574, 670).trim(),
				linha.substring(670, 690).trim(), linha.substring(690, 740).trim(), linha.substring(740, 804).trim(),
				linha.substring(804, 868).trim(), linha.substring(868, 870).trim(), linha.substring(870, 878).trim(),
				linha.substring(878, 958).trim(), linha.substring(958, 978).trim(), linha.substring(978, 998).trim(),
				linha.substring(998, 1018).trim(), linha.substring(1018, 1038).trim(),
				linha.substring(1038, 1166).trim());
		count++;
		// System.out.println(
		// c.getNome() + "," + c.getCnpjCpf() + "," + c.getNomeContador() +
		// ", linhas lidas:" + count);
		mapContribuinte.put(c.getInscricao().trim(), c);

		// pessoa
		try {
			Pessoa p = new Pessoa();
			p.setPessoaId(Long.valueOf(count));
			p.setBairro(c.getBairro());
			p.setCelular(util.getLimpaTelefone(c.getCelular()));
			p.setCep(util.trataCep(c.getCep()));
			p.setCnpjCpf(util.getCpfCnpj(c.getCnpjCpf()));
			p.setComplemento(c.getComplemento());
			p.setEmail(util.trataEmail(c.getEmail()));

			p.setEndereco(util.trataLogradouro(c.getLocalizacao()));
			p.setInscricaoMunicipal(c.getInscricao());

			p.setMunicipio(c.getCidade());
			p.setUf(c.getEstado());

			try {
				p.setMunicipioIbge(Long.valueOf(municipiosIbgeDao.getCodigoIbge(p.getMunicipio(), p.getUf())));
			} catch (Exception e) {
				System.out.println("Código Ibge não encontrado para:" + p.getMunicipio() + "-" + p.getUf());
				e.printStackTrace();
			}
			p.setNome(c.getNome());
			p.setNomeFantasia(c.getNomeFantasia());
			p.setNumero(util.trataNumeroEndereco(c.getNumero()));
			p.setOptanteSimples(c.getIndicaSimplesNacional().substring(0, 1));
			p.setTelefone(util.getLimpaTelefone(c.getTelefone()));
			p.setTipoPessoa(c.getTipoJuridico());
			util.trataNumerosTelefones(p);
			util.anulaCamposVazios(p);
			pessoaDao.save(p);
			// prestadores
			Prestadores pr = null;
			try {
				pr = new Prestadores();
				pr.setAutorizado("N");
				pr.setCelular(p.getCelular());
				pr.setEmail(p.getEmail());
				pr.setEnquadramento("N");
				pr.setFax(util.getLimpaTelefone(c.getFax()));
				pr.setInscricaoPrestador(p.getCnpjCpf());
				pr.setTelefone(p.getTelefone());
				pr.setInscricaoMunicipal(p.getInscricaoMunicipal());
				pr.setMotivo("Solicitar cadastro");
				util.trataNumerosTelefones(pr);
				util.anulaCamposVazios(pr);
				prestadoresDao.save(pr);

				if (!util.isEmptyOrNull(p.getOptanteSimples()) && p.getOptanteSimples().equals("S")) {

					PrestadoresOptanteSimples po = new PrestadoresOptanteSimples();
					po.setDataEfeito(!util.isEmptyOrNull(c.getDataAutorizacao())
							? util.getStringToDate(c.getDataAutorizacao()) : util.getStringToDate(c.getDataAbertura()));
					po.setDataInicio(!util.isEmptyOrNull(c.getDataAutorizacao())
							? util.getStringToDate(c.getDataAutorizacao()) : util.getStringToDate(c.getDataAbertura()));
					po.setDescricao("Optante simples");
					po.setInscricaoPrestador(p.getCnpjCpf());
					po.setMotivo("Opção do Contribuinte");
					if (c.getRegimeIssqn().equalsIgnoreCase("mei")) {
						po.setMei("S");
					} else {
						po.setMei("N");
					}
					po.setOptante("S");
					po.setOrgao("M");
					po.setPrestadores(pr);
					prestadoresOptanteSimplesDao.save(po);
				}
			} catch (Exception e) {
				log.fillError(linha, "Prestador não gravado", e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			log.fillError(linha, "Pessoa não gravada", e);
			e.printStackTrace();
		}

	}

}
