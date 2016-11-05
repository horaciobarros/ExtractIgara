package br.com.jway.igaratinga.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import br.com.jway.igaratinga.dao.CnaeAtualizadoDao;
import br.com.jway.igaratinga.dao.DadosLivroPrestadorDao;
import br.com.jway.igaratinga.dao.GuiasDao;
import br.com.jway.igaratinga.dao.GuiasNotasFiscaisDao;
import br.com.jway.igaratinga.dao.MunicipiosIbgeDao;
import br.com.jway.igaratinga.dao.NotasFiscaisCanceladasDao;
import br.com.jway.igaratinga.dao.NotasFiscaisDao;
import br.com.jway.igaratinga.dao.NotasFiscaisEmailsDao;
import br.com.jway.igaratinga.dao.NotasFiscaisPrestadoresDao;
import br.com.jway.igaratinga.dao.NotasFiscaisServicosDao;
import br.com.jway.igaratinga.dao.NotasFiscaisTomadoresDao;
import br.com.jway.igaratinga.dao.PrestadoresAtividadesDao;
import br.com.jway.igaratinga.dao.PrestadoresDao;
import br.com.jway.igaratinga.dao.ServicosAliquotaDao;
import br.com.jway.igaratinga.dao.ServicosDao;
import br.com.jway.igaratinga.dao.TomadoresDao;
import br.com.jway.igaratinga.entidadesOrigem.DadosLivroPrestador;
import br.com.jway.igaratinga.entidadesOrigem.Servicos;
import br.com.jway.igaratinga.entidadesOrigem.ServicosAliquota;
import br.com.jway.igaratinga.model.CnaeAtualizado;
import br.com.jway.igaratinga.model.Guias;
import br.com.jway.igaratinga.model.GuiasNotasFiscais;
import br.com.jway.igaratinga.model.NotasFiscais;
import br.com.jway.igaratinga.model.NotasFiscaisCanceladas;
import br.com.jway.igaratinga.model.NotasFiscaisEmails;
import br.com.jway.igaratinga.model.NotasFiscaisPrestadores;
import br.com.jway.igaratinga.model.NotasFiscaisServicos;
import br.com.jway.igaratinga.model.NotasFiscaisTomadores;
import br.com.jway.igaratinga.model.Prestadores;
import br.com.jway.igaratinga.model.PrestadoresAtividades;
import br.com.jway.igaratinga.model.Tomadores;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class NotasMaeThread implements Runnable {

	private DadosLivroPrestador dlp;
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private Util util;
	private NotasFiscaisDao notasFiscaisDao = new NotasFiscaisDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();
	private GuiasDao guiasDao = new GuiasDao();
	private FileLog log;
	private DadosLivroPrestadorDao dlpDao = new DadosLivroPrestadorDao();
	private NotasFiscaisTomadoresDao notasFiscaisTomadoresDao = new NotasFiscaisTomadoresDao();
	private GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();
	private NotasFiscaisPrestadoresDao notasFiscaisPrestadoresDao = new NotasFiscaisPrestadoresDao();
	private NotasFiscaisEmailsDao notasFiscaisEmailsDao = new NotasFiscaisEmailsDao();
	private NotasFiscaisCanceladasDao notasFiscaisCanceladasDao = new NotasFiscaisCanceladasDao();
	private PrestadoresAtividadesDao prestadoresAtividadesDao = new PrestadoresAtividadesDao();
	private CnaeAtualizadoDao cnaeAtualizadoDao = new CnaeAtualizadoDao();
	private NotasFiscaisServicosDao notasFiscaisServicosDao = new NotasFiscaisServicosDao();
	private ServicosAliquotaDao servicosAliquotaDao = new ServicosAliquotaDao();

	public NotasMaeThread(DadosLivroPrestador dlp, Util util, FileLog log) {
		this.dlp = dlp;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		
		try {
			String inscricaoPrestador = dlp.getCnpjPrestador().trim();
			Prestadores pr = prestadoresDao.findByInscricao(inscricaoPrestador);

			if (pr == null){
				throw new Exception("Prestador não encontrado: "+inscricaoPrestador+" nota: "+dlp.getNumeroNota());
			}
			
			NotasFiscais nf = new NotasFiscais();
			try {
				nf.setDataHoraEmissao(util.getStringToDateHoursMinutesWithBars(dlp.getDataEmissao()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			nf.setNomeArquivo(dlp.getNomeArquivo());
			nf.setInscricaoPrestador(dlp.getCnpjPrestador());
			nf.setNaturezaOperacao(dlp.getNaturezaOperacao());
			nf.setNomePrestador(dlp.getRazaoSocialPrestador());
			nf.setNomeTomador(dlp.getRazaoSocialTomador());
			if (util.isEmptyOrNull(nf.getNomeTomador())) {
				nf.setNomeTomador("Não informado");
			}
			nf.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			nf.setOptanteSimples(dlp.getOptantePeloSimplesNacional().trim().equals("1") ? "S" : "N");
			nf.setPrestadores(pr);
			if ("J".equals(util.getTipoPessoa(pr.getInscricaoPrestador()))) {
				nf.setValorCofins(BigDecimal.valueOf(dlp.getValorCofins()));
				nf.setValorCsll(BigDecimal.valueOf(dlp.getValorCsll()));
				nf.setValorPisPasep(BigDecimal.valueOf(dlp.getValorPis()));
				nf.setValorInss(BigDecimal.valueOf(dlp.getValorInss()));
				nf.setValorIr(BigDecimal.valueOf(dlp.getValorIr()));
				nf.setValorOutrasRetencoes(BigDecimal.valueOf(dlp.getValorOutrasRetencoes()));
			}			
			
			nf.setValorTotalIssOptante(BigDecimal.valueOf(dlp.getValorIss()));
			nf.setValorTotalServico(BigDecimal.valueOf(dlp.getValorTotalNfse()));
			nf.setValorTotalIss(BigDecimal.valueOf(dlp.getValorIss()));
			nf.setValorTotalBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
			nf.setValorPisPasep(BigDecimal.valueOf(dlp.getValorPis()));
			try {
				nf.setValorTotalDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				nf.setValorLiquido(BigDecimal.valueOf(dlp.getValorLiquidoNfse()));
			}
			catch(Exception e){
				e.printStackTrace();
				log.fillError(dlp.getNomeArquivo(),"Nota fiscal sem valor líquido ");
				nf.setValorLiquido(BigDecimal.valueOf(dlp.getValorServico()));
			}

			try {
				if (dlp.getNfseSubstituta() != null && !util.isEmptyOrNull(dlp.getNfseSubstituta().toString())) {
					nf.setSituacaoOriginal("S");
					nf.setNumeroNotaFiscalSubstituta(dlp.getNfseSubstituta().toString());
				} else if (!util.isEmptyOrNull(dlp.getNumeroCancelada())) {
					nf.setSituacaoOriginal("C");
				} else {
					nf.setSituacaoOriginal("N");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			nf.setSituacao("N");
			
			try {
				nf.setSituacaoTributaria(util.getSituacaoTributaria(dlp));
			} catch (Exception e) {
				nf.setSituacaoTributaria("N");
			}
			
			try {
				if (dlp.getCodigoVerificacao() != null && !dlp.getCodigoVerificacao().trim().isEmpty()) {
					if (dlp.getCodigoVerificacao().length() > 9) {
						nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim().substring(0, 9));
					} else {
						nf.setNumeroVerificacao(dlp.getCodigoVerificacao().trim());
					}
				} else {
					nf.setNumeroVerificacao(util.completarZerosEsquerda(dlp.getIdCodigo().toString(), 9));
				}
			} catch (Exception e) {
				e.printStackTrace();

			}

			nf.setServicoPrestadoForaPais("N");
			// nf.setDataHoraRps(nf.getDataHoraEmissao());

			// tomadores
			String inscricaoTomador = util.getCpfCnpj(dlp.getCnpjTomador());

			if (util.isEmptyOrNull(inscricaoTomador)) {
				inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
			}
			if (util.getTipoPessoa(inscricaoTomador).equals("F")) {
				if (!Util.validarCpf(inscricaoTomador)) {
					inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
				}
			} else if (util.getTipoPessoa(inscricaoTomador).equals("J")) {
				if (!Util.validarCnpj(inscricaoTomador)) {
					inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
				}
			} else {
				inscricaoTomador = Util.CPF_TOMADOR_FICTICIO;
			}

			nf.setInscricaoTomador(inscricaoTomador);

			nf = notasFiscaisDao.save(nf);

			Tomadores t = null;

			if (!util.isEmptyOrNull(nf.getInscricaoTomador())) {
				t = tomadoresDao.findByInscricao(nf.getInscricaoTomador(), nf.getInscricaoPrestador());
				if (t == null || t.getId() == null) {
					try {
						t = new Tomadores();
						gravaTomadores(dlp, nf, t);
					} catch (Exception e) {
						log.fillError(dlp.toString(), "Tomadores ", e);
						e.printStackTrace();
						t = null;
					}

				} else {
					if (nf.getDataHoraEmissao().getTime() > t.getDataAtualizacao().getTime()) {
						gravaTomadores(dlp, nf, t);
					}

				}
			}

			Guias g = null;
			/*
			 * if (dlp.getNossoNumero() != null &&
			 * !dlp.getNossoNumero().trim().isEmpty()) { g =
			 * guiasDao.findByNumeroGuia(dlp.getNossoNumero()); }
			 * 
			 * if (g != null && g.getId() != null &&
			 * !nf.getSituacaoOriginal().equals("C") &&
			 * !nf.getNaturezaOperacao().equals("3") &&
			 * !nf.getOptanteSimples().equals("S")) { nf.setSituacao("E"); }
			 * else { nf.setSituacao("N"); }
			 */

			processaDemaisTiposNotas(pr, nf, dlp, log, dlp.getNomeArquivo(), t, g);

		} catch (Exception e) {
			log.fillError(dlp.getNomeArquivo(), "Nota Fiscal ", e);
			e.printStackTrace();
		}

	}

	private void gravaTomadores(DadosLivroPrestador dlp2, NotasFiscais nf, Tomadores t) {
		try {
			t.setOptanteSimples(util.getOptantePeloSimplesNacional("N"));
			if (!util.isEmptyOrNull(dlp.getRazaoSocialTomador())) {
				t.setNome(dlp.getRazaoSocialTomador());
				t.setNomeFantasia(dlp.getRazaoSocialTomador());
			} else {
				t.setNome("Não informado");
				t.setNomeFantasia("Não informado");
			}
			t.setPrestadores(nf.getPrestadores());
			t.setTipoPessoa(util.getTipoPessoa(nf.getInscricaoTomador()));
			t.setInscricaoTomador(nf.getInscricaoTomador());
			t.setBairro(util.getNullIfEmpty(dlp.getEnderecoBairroTomador()));
			t.setCep(util.trataCep(dlp.getCepTomador()));
			t.setComplemento(util.getNullIfEmpty(dlp.getEnderecoComplementoTomador()));
			t.setEmail(util.trataEmail(dlp.getEmailTomador()));
			if (!util.isEmptyOrNull(t.getEmail()) && t.getEmail().length() > 80) {
				t.setEmail(t.getEmail().substring(0, 80));
			}
			t.setEndereco(util.trataLogradouro(dlp.getEnderecoTomador()));
			t.setInscricaoEstadual(dlp.getInscricaoEstadualTomador());
			t.setInscricaoMunicipal(util.getStringLimpa(dlp.getInscricaoMunicipalTomador()));
			try{
				t.setMunicipioIbge(Long.parseLong(dlp.getEnderecoTomadorCodigoMunicipio()+""));
			}
			catch(Exception e){
				
			}
			t.setMunicipio(dlp.getMunicipioTomador());
			if (t.getMunicipioIbge() != null) {
				t.setMunicipio(municipiosIbgeDao.findMunicipioByIbge(t.getMunicipioIbge()+""));
			}
			else if (!util.isEmptyOrNull(t.getMunicipio())){
				t.setMunicipioIbge(Long.parseLong(municipiosIbgeDao.getCodigoIbge(t.getMunicipio(), "MG")));
			}
			t.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));
			t.setDataAtualizacao(nf.getDataHoraEmissao());
			try {
				t.setMunicipioIbge(
						Long.valueOf(municipiosIbgeDao.getCodigoIbge(dlp.getMunicipioTomador(), dlp.getUfTomador())));
			} catch (Exception e) {
				// log.fillError(linha,"Nota Fiscal Tomadores ", e);
				// e.printStackTrace();
			}
			util.trataNumerosTelefones(t);
			util.anulaCamposVazios(t);

			t.setDataAtualizacao(nf.getDataHoraEmissao());

			if (t.getId() == null) {
				t = tomadoresDao.save(t);
			} else {
				t = tomadoresDao.update(t);
			}

		} catch (Exception e) {
			log.fillError(dlp.toString(), "Tomadores ", e);
			e.printStackTrace();
			t = null;
		}

	}

	private void processaDemaisTiposNotas(Prestadores pr, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, Tomadores t, Guias g) {
		// -- serviÃ§os

		try {
			processaNotasFilhaServico(pr, nf, dlp, log, linha, "S");

			// -- canceladas
			if ("C".equals(nf.getSituacaoOriginal())) {
				processaNotasFilhaCancelada(pr, nf, dlp, log, linha, "C");
			}

			// email
			if (dlp.getEmailPrestador() != null && !dlp.getEmailPrestador().isEmpty()) {
				processaNotasFilhaEmail(pr, nf, dlp, log, linha, "E");
			}

			// notas-fiscais-cond-pagamentos ??

			// notas-fiscais-obras ??

			// notas-fiscais-prestadores
			processaNotasFilhaPrestadores(pr, nf, dlp, log, linha, "P");

			// guias x notas fiscais

			if (nf.getSituacao().equals("E")) { // guia emitida
				processaNotasFilhaGuias(pr, nf, dlp, log, linha, "G", g);

			} else {
				// log.fillError(linha, "Numero de guia nÃ£o encontrado: " +
				// dlp.getNossoNumero());

			}

			// notas fiscais tomadores

			if (t != null && t.getId() != null) {
				processaNotasFilhaTomadores(pr, nf, dlp, log, linha, "T", g, t);
			}
			
		} catch (Exception e) {
			log.fillError(linha, "Nota Fiscal Servico: " + nf.getInscricaoPrestador()  + " - Nota: " + nf.getNumeroNota(), e);
			e.printStackTrace();
		}

	}

	private void processaNotasFilhaTomadores(Prestadores pr, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String string, Guias g, Tomadores t) {
		try {
			NotasFiscaisTomadores nft = new NotasFiscaisTomadores();
			nft.setBairro(util.getStringLimpa(dlp.getEnderecoBairroTomador()));
			nft.setCelular(t.getCelular());
			nft.setCep(dlp.getCepTomador());
				
			nft.setComplemento(util.getStringLimpa(dlp.getEnderecoComplementoTomador()));
			nft.setEmail(util.trataEmail(dlp.getEmailTomador()));
			if (!util.isEmptyOrNull(nft.getEmail()) && nft.getEmail().length() > 80) {
				nft.setEmail(nft.getEmail().substring(0, 80));
			}
			nft.setEndereco(util.trataLogradouro(dlp.getEnderecoTomador()));
			nft.setInscricaoEstadual(t.getInscricaoEstadual());
			nft.setInscricaoMunicipal(t.getInscricaoMunicipal());
			nft.setInscricaoPrestador(nf.getInscricaoPrestador());
			nft.setInscricaoTomador(t.getInscricaoTomador());
			nft.setMunicipio(t.getMunicipio());
			if (t.getMunicipioIbge() != null) {
				nft.setMunicipioIbge(Long.toString(t.getMunicipioIbge()));
			}
			nft.setNome(nf.getNomeTomador());
			nft.setNomeFantasia(nf.getNomeTomador());
			nft.setNotasFiscais(nf);
			nft.setNumero(util.getStringLimpa(dlp.getEnderecoNumeroTomador()));
			nft.setNumeroNota(nf.getNumeroNota());
			nft.setOptanteSimples(t.getOptanteSimples());
			nft.setTipoPessoa(t.getTipoPessoa());
			nft.setTelefone(util.getLimpaTelefone(dlp.getTelefoneTomador()));
			notasFiscaisTomadoresDao.save(nft);
		} catch (Exception e) {

			e.printStackTrace();
			log.fillError(dlp.toString(), "Nota Fiscal Tomadores ", e);
		}
	}

	private void processaNotasFilhaGuias(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String string, Guias guia) {
		try {
			if (dlp.getOptantePeloSimplesNacional().trim().substring(0, 1).equals("S")) {
				return;
			}

			GuiasNotasFiscais gnf = new GuiasNotasFiscais();
			gnf.setGuias(guia);
			gnf.setInscricaoPrestador(p.getInscricaoPrestador());
			// gnf.setNumeroGuia(g.getNumeroGuia());
			gnf.setNumeroGuia(guia.getNumeroGuia()); // acertar depois
			gnf.setNumeroNota(nf.getNumeroNota());
			gnf.setSituacaoTributaria(nf.getSituacaoTributaria());
			guiasNotasFiscaisDao.save(gnf);
		} catch (Exception e) {
			e.printStackTrace();
			String idGuia = "";
			String idNota = nf.getId() + "";
			if (guia != null && guia.getId() != null && guia.getId() > 0) {
				idGuia = guia.getId() + "";
			}
			log.fillError(linha, "Guia Nota Fiscal: " + idGuia + " - Nota: " + idNota + " - ", e);
		}

	}

	private void processaNotasFilhaPrestadores(Prestadores pr, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String tipoNotaFilha) {
		try {
			NotasFiscaisPrestadores nfp = new NotasFiscaisPrestadores();
			nfp.setBairro(dlp.getEnderecoBairroPrestador());
			nfp.setTelefone(util.getLimpaTelefone(dlp.getTelefonePrestador()));
			if (util.isEmptyOrNull(nfp.getTelefone()) ||nfp.getTelefone().length() < 10) {
				nfp.setTelefone(null);
			}
			nfp.setCep(dlp.getCepPrestador());
			nfp.setComplemento(util.getNullIfEmpty(dlp.getEnderecoComplementoPrestador()));
			nfp.setEmail(util.trataEmail(dlp.getEmailPrestador()));
			nfp.setEndereco(dlp.getEnderecoPrestador());
			nfp.setInscricaoPrestador(pr.getInscricaoPrestador());
			nfp.setNome(dlp.getRazaoSocialPrestador());
			nfp.setNomeFantasia(dlp.getNomeFantasiaPrestador());
			nfp.setNotasFiscais(nf);
			nfp.setNumero(dlp.getEnderecoNumeroPrestador());
			nfp.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			nfp.setOptanteSimples(dlp.getOptantePeloSimplesNacional().trim().equals("1") ? "S" : "N");
			nfp.setTipoPessoa(util.getTipoPessoa(pr.getInscricaoPrestador()));
			nfp.setInscricaoMunicipal(pr.getInscricaoMunicipal());
			notasFiscaisPrestadoresDao.save(nfp);

		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Prestador ", e);
		}

	}

	private void processaNotasFilhaEmail(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String string) {
		try {
			NotasFiscaisEmails nfe = new NotasFiscaisEmails();
			nfe.setEmail(dlp.getEmailPrestador());
			nfe.setInscricaoPrestador(p.getInscricaoPrestador());
			nfe.setNotasFiscais(nf);
			nfe.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			notasFiscaisEmailsDao.save(nfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Email ", e);
		}

	}

	private void processaNotasFilhaCancelada(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String tipoNotaFilha) {
		try {
			NotasFiscaisCanceladas nfc = new NotasFiscaisCanceladas();
			if (!util.isEmptyOrNull(dlp.getDataHoraCancelamento())) {
				nfc.setDatahoracancelamento(util.getStringToDateHours(dlp.getDataHoraCancelamento()));
				if (nfc.getDatahoracancelamento().getTime() < nf.getDataHoraEmissao().getTime()) {
					nfc.setDatahoracancelamento(nf.getDataHoraEmissao());
				}
			} else {
				nfc.setDatahoracancelamento(nf.getDataHoraEmissao());
			}

			nfc.setInscricaoPrestador(util.getCpfCnpj(dlp.getCnpjPrestador()));
			nfc.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));
			nfc.setMotivo(dlp.getMotivoCancelamento());
			if (util.isEmptyOrNull(nfc.getMotivo())) {
				nfc.setMotivo("Dados incorretos");
			}
			nfc.setNotasFiscais(nf);
			notasFiscaisCanceladasDao.save(nfc);

		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(linha, "Nota Fiscal Cancelada ", e);
		}

	}

	private void processaNotasFilhaServico(Prestadores p, NotasFiscais nf, DadosLivroPrestador dlp, FileLog log,
			String linha, String tipoNotaFilha) {

		try {

			NotasFiscaisServicos nfs = new NotasFiscaisServicos();
			nfs.setInscricaoPrestador(util.getCpfCnpj(dlp.getCnpjPrestador()));
			nfs.setNumeroNota(Long.valueOf(dlp.getNumeroNota()));

			try{
				nfs.setMunicipioIbge(dlp.getCodigoMunicipioServico()+"");
			}
			catch(Exception e){
				nfs.setMunicipioIbge(Util.CODIGO_IBGE);
				e.printStackTrace();
			}

			nfs.setItemListaServico(dlp.getItemListaServico());

			String cnae = util.getStringLimpa(dlp.getCodigoCnae());

			if (!util.isEmptyOrNull(nfs.getItemListaServico())) {
				nfs.setItemListaServico(dlp.getItemListaServico());
			}

			CnaeAtualizado c = null;
			if (!util.isEmptyOrNull(cnae)) {
				c = new CnaeAtualizadoDao().findByCodigo(cnae);
				if (c != null && !util.isEmptyOrNull(c.getDenominacao())) {
					nfs.setDescricaoCnae(c.getDenominacao());
					nfs.setIcnaes(c.getCnae());
				} else {
					nfs.setIcnaes(util.completarZerosDireita(cnae, 7));
				}
			}

			nfs.setDescricao(dlp.getDiscriminacaoServico());
			if (util.isEmptyOrNull(nfs.getDescricao().trim())) {
				nfs.setDescricao("Serviços Diversos");
			}

			nfs.setAliquota(BigDecimal.valueOf(dlp.getValorAliquota()).multiply(BigDecimal.valueOf(100)));
			nfs.setQuantidade(BigDecimal.valueOf(1));
			nfs.setValorServico(BigDecimal.valueOf(dlp.getValorServico()));
			nfs.setValorDeducao(BigDecimal.valueOf(dlp.getValorDeducao()));
			nfs.setValorDescCondicionado(BigDecimal.valueOf(dlp.getValorDescontoCondicionado()));
			nfs.setValorDescIncondicionado(BigDecimal.valueOf(dlp.getValorDescontoIncondicionado()));
			nfs.setValorBaseCalculo(BigDecimal.valueOf(dlp.getValorBaseCalculo()));
			nfs.setValorIss(BigDecimal.valueOf(dlp.getValorIss()));
			nfs.setNotasFiscais(nf);
			nfs.setValorUnitario(BigDecimal.valueOf(dlp.getValorServico()));
			if (nfs.getAliquota().compareTo(BigDecimal.ZERO) == 0) {
				nfs.setAliquota(BigDecimal.valueOf(1));
			}
			notasFiscaisServicosDao.save(nfs);
			ServicosDao dao = new ServicosDao();
			Servicos serv = dao.findByCodigoServicoCodigoCnae(nfs.getItemListaServico(), nfs.getIcnaes());
			ServicosAliquota servAliquota = servicosAliquotaDao.findByCodigo(nfs.getItemListaServico());
			if (serv == null || serv.getId() == null) {
				Servicos s = new Servicos();
				if (servAliquota!=null){
					s.setAliquota(servAliquota.getAliquota());
				}
				else{
					s.setAliquota("" + nfs.getAliquota().doubleValue());
				}
				s.setCnaes(nfs.getIcnaes());
				s.setCodigo(nfs.getItemListaServico());
				s.setNome(nfs.getDescricaoCnae());
				s.setDataAtualizacao(nf.getDataHoraEmissao());
				dao.save(s);
			}
			
			PrestadoresAtividades pa = prestadoresAtividadesDao.findByInscricaoItemListaServicoCnae(nfs.getInscricaoPrestador(), nfs.getItemListaServico(), nfs.getIcnaes());
			if (pa == null || pa.getId()==null){
				pa = new PrestadoresAtividades();
				if (servAliquota!=null){
					pa.setAliquota(util.getStringToBigDecimal(servAliquota.getAliquota()));
				}
				else{
					pa.setAliquota(nfs.getAliquota());
				}
				pa.setIcnaes(nfs.getIcnaes());
				pa.setIlistaservicos(nfs.getItemListaServico());
				pa.setCodigoAtividade(nfs.getItemListaServico());
				pa.setInscricaoPrestador(nfs.getInscricaoPrestador());
				pa.setPrestadores(p);
				prestadoresAtividadesDao.save(pa);
			}
		} catch (Exception e) {
			log.fillError(dlp.getNomeArquivo(), "Nota Fiscal Serviço ", e);
			e.printStackTrace();
		}
	}

}
