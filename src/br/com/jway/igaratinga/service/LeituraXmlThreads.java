package br.com.jway.igaratinga.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import nfe.entidades.layoutnfse.TcCancelamentoNfse;
import nfe.entidades.layoutnfse.TcNfse;
import nfe.utilitarios.BuildObjetcts;

import org.xml.sax.SAXException;

import br.com.jway.igaratinga.entidadesOrigem.DadosLivroPrestador;
import br.com.jway.igaratinga.util.FileLog;
import br.com.jway.igaratinga.util.Util;

public class LeituraXmlThreads implements Runnable {

	private File f;
	private ExecutorService executor;
	private FileLog log;
	private Util util;
	private SimpleDateFormat formatterData = new SimpleDateFormat("dd/MM/yyyy hh:mm");

	public LeituraXmlThreads(File f, ExecutorService executor, FileLog log, Util util) {
		this.f = f;
		this.executor = executor;
		this.log = log;
		this.util = util;
	}

	@Override
	public void run() {
		try {
			// System.out.println(f.getName());
			String xml = lerXML(f);
			// System.out.println(xml);
			if (isNFSe(xml)) {
				TcNfse nfse;
				try {
					nfse = BuildObjetcts.getTcNfse(xml);
					TcCancelamentoNfse cancelamentoNfse = null;
					if (isNfseCancelamento(xml)) {
						cancelamentoNfse = BuildObjetcts.getTcNfseCancelamento(xml);
					}

					if (nfse != null && nfse.getInfNfse() != null) {
						criaEntidades(nfse, cancelamentoNfse);
					}

				} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
					log.fillError(xml, "Erro na leitura de xml ", e);
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(f.getName(), "Erro na leitura de xml ", e);
		}
	}

	private void criaEntidades(TcNfse nfse, TcCancelamentoNfse cancelamentoNfse) {
		try {
			DadosLivroPrestador dados = new DadosLivroPrestador();
			dados.setNomeArquivo(f.getName());
			if (nfse.getInfNfse() != null) {
				// nfse.getInfNfse().getValorCredito();
				dados.setStatusNota("N");

				dados.setCodigoVerificacao(nfse.getInfNfse().getCodigoVerificacao());
				if (nfse.getInfNfse().getPrestadorServico() != null) {

					dados.setNomeFantasiaPrestador(nfse.getInfNfse().getPrestadorServico().getNomeFantasia());
					dados.setRazaoSocialPrestador(nfse.getInfNfse().getPrestadorServico().getRazaoSocial());

					if (nfse.getInfNfse().getPrestadorServico().getContato() != null) {

						dados.setEmailPrestador(nfse.getInfNfse().getPrestadorServico().getContato().getEmail());
						dados.setTelefonePrestador(nfse.getInfNfse().getPrestadorServico().getContato().getTelefone());

					}
					if (nfse.getInfNfse().getPrestadorServico().getIdentificacaoPrestador() != null) {
						if (nfse.getInfNfse().getPrestadorServico().getIdentificacaoPrestador().getCnpj() != null) {
							dados.setCnpjPrestador(
									nfse.getInfNfse().getPrestadorServico().getIdentificacaoPrestador().getCnpj());
						}
						if (nfse.getInfNfse().getPrestadorServico().getIdentificacaoPrestador()
								.getInscricaoMunicipal() != null) {
							dados.setInscricaoMunicipalPrestador(nfse.getInfNfse().getPrestadorServico()
									.getIdentificacaoPrestador().getInscricaoMunicipal());
						}
					}

					if (nfse.getInfNfse().getPrestadorServico().getEndereco() != null) {
						if (nfse.getInfNfse().getPrestadorServico().getEndereco().getCep() != null) {
							dados.setCepPrestador("" + nfse.getInfNfse().getPrestadorServico().getEndereco().getCep());
						}

						dados.setEnderecoPrestadorCodigoMunicipio(
								nfse.getInfNfse().getPrestadorServico().getEndereco().getCodigoMunicipio());
						dados.setEnderecoBairroPrestador(
								nfse.getInfNfse().getPrestadorServico().getEndereco().getBairro());
						dados.setEnderecoComplementoPrestador(
								nfse.getInfNfse().getPrestadorServico().getEndereco().getComplemento());
						dados.setEnderecoPrestador(nfse.getInfNfse().getPrestadorServico().getEndereco().getEndereco());
						dados.setEnderecoNumeroPrestador(
								nfse.getInfNfse().getPrestadorServico().getEndereco().getNumero());
						dados.setUfPrestador(nfse.getInfNfse().getPrestadorServico().getEndereco().getUf());
					}
				}
				if (nfse.getInfNfse().getTomadorServico() != null) {

					dados.setRazaoSocialTomador(nfse.getInfNfse().getTomadorServico().getRazaoSocial());

					if (nfse.getInfNfse().getTomadorServico().getContato() != null) {

						dados.setEmailTomador(nfse.getInfNfse().getTomadorServico().getContato().getEmail());
						dados.setTelefoneTomador(nfse.getInfNfse().getTomadorServico().getContato().getTelefone());

					}
					if (nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador() != null) {
						if (nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador().getCpfCnpj() != null) {

							dados.setInscricaoMunicipalTomador(nfse.getInfNfse().getTomadorServico()
									.getIdentificacaoTomador().getInscricaoMunicipal());

							if (nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador().getCpfCnpj()
									.getCnpj() != null) {
								if (!nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador().getCpfCnpj()
										.getCnpj().isEmpty()) {
									dados.setCnpjTomador(nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador()
											.getCpfCnpj().getCnpj());
								}
							}
							if (nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador().getCpfCnpj()
									.getCpf() != null) {
								if (!nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador().getCpfCnpj()
										.getCpf().isEmpty()) {
									dados.setCnpjTomador(nfse.getInfNfse().getTomadorServico().getIdentificacaoTomador()
											.getCpfCnpj().getCpf());
								}
							}
						}
					}

					if (nfse.getInfNfse().getTomadorServico().getEndereco() != null) {
						if (nfse.getInfNfse().getTomadorServico().getEndereco().getCep() != null) {
							dados.setCepTomador("" + nfse.getInfNfse().getTomadorServico().getEndereco().getCep());
						}

						dados.setEnderecoBairroTomador(nfse.getInfNfse().getTomadorServico().getEndereco().getBairro());
						dados.setEnderecoComplementoTomador(
								nfse.getInfNfse().getTomadorServico().getEndereco().getComplemento());
						dados.setEnderecoTomador(nfse.getInfNfse().getTomadorServico().getEndereco().getEndereco());
						dados.setEnderecoNumeroTomador(nfse.getInfNfse().getTomadorServico().getEndereco().getNumero());
						dados.setUfTomador(nfse.getInfNfse().getTomadorServico().getEndereco().getUf());
						dados.setEnderecoTomadorCodigoMunicipio(
								nfse.getInfNfse().getTomadorServico().getEndereco().getCodigoMunicipio());
					}
				}
				try {
					dados.setEnderecoTomadorCodigoMunicipio(
							nfse.getInfNfse().getTomadorServico().getEndereco().getCodigoMunicipio());
				} catch (Exception e) {
				}

				dados.setCompetencia(XMLCalendarToString(nfse.getInfNfse().getCompetencia()));
				if (nfse.getInfNfse().getContrucaoCivil() != null) {
					dados.setConstrucaoCodigoObra(nfse.getInfNfse().getContrucaoCivil().getCodigoObra());
					dados.setConstrucaoArt(nfse.getInfNfse().getContrucaoCivil().getArt());
				}

				dados.setDataEmissao(XMLCalendarToString(nfse.getInfNfse().getDataEmissao()));
				dados.setNaturezaOperacao(nfse.getInfNfse().getNaturezaOperacao() + "");
				if (nfse.getInfNfse().getDataEmissaoRps() != null) {
					dados.setDataEmissaoRps(XMLCalendarToString(nfse.getInfNfse().getDataEmissaoRps()));
				}
				// ;
				if (nfse.getInfNfse().getIdentificacaoRps() != null) {
					dados.setNumeroRps(nfse.getInfNfse().getIdentificacaoRps().getNumero());
					dados.setSerieRps(nfse.getInfNfse().getIdentificacaoRps().getSerie());
					dados.setTipoRps(nfse.getInfNfse().getIdentificacaoRps().getTipo() + "");
				}
				dados.setIncentivadorCultural(nfse.getInfNfse().getIncentivadorCultural() + "");

				// nfse.getInfNfse().getIntermediarioServico().getCpfCnpj();
				// nfse.getInfNfse().getIntermediarioServico().getInscricaoMunicipal();
				// nfse.getInfNfse().getIntermediarioServico().getRazaoSocial();
				dados.setNfseSubstituta(nfse.getInfNfse().getNfseSubstituida());
				//System.out.println(nfse.getInfNfse().getNfseSubstituida() != null ? dados.getNfseSubstituta() : "");

				dados.setNumeroNota(nfse.getInfNfse().getNumero() + "");
				dados.setOptantePeloSimplesNacional(nfse.getInfNfse().getOptanteSimplesNacional() + "");

				// nfse.getInfNfse().getOrgaoGerador().getCodigoMunicipio();
				// nfse.getInfNfse().getOrgaoGerador().getUf();
				dados.setOutrasInformacoes(nfse.getInfNfse().getOutrasInformacoes());
				dados.setRegimeTributacao(nfse.getInfNfse().getRegimeEspecialTributacao() + "");

				if (nfse.getInfNfse().getServico() != null) {
					dados.setCodigoCnae(nfse.getInfNfse().getServico().getCodigoCnae() + "");
					dados.setCodigoMunicipioServico(nfse.getInfNfse().getServico().getCodigoMunicipio());
					dados.setCodigoTributacaoMunicipio(nfse.getInfNfse().getServico().getCodigoTributacaoMunicipio());
					dados.setDiscriminacaoServico(nfse.getInfNfse().getServico().getDiscriminacao());
					dados.setItemListaServico(nfse.getInfNfse().getServico().getItemListaServico());

					if (nfse.getInfNfse().getServico().getValores().getAliquota() != null) {
						dados.setValorAliquota(nfse.getInfNfse().getServico().getValores().getAliquota().doubleValue());
					} else {
						dados.setValorAliquota(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getBaseCalculo() != null) {
						dados.setValorBaseCalculo(
								nfse.getInfNfse().getServico().getValores().getBaseCalculo().doubleValue());
					} else {
						dados.setValorBaseCalculo(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getDescontoCondicionado() != null) {
						dados.setValorDescontoCondicionado(
								nfse.getInfNfse().getServico().getValores().getDescontoCondicionado().doubleValue());
					} else {
						dados.setValorDescontoCondicionado(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getDescontoIncondicionado() != null) {
						dados.setValorDescontoIncondicionado(
								nfse.getInfNfse().getServico().getValores().getDescontoIncondicionado().doubleValue());
					} else {
						dados.setValorDescontoIncondicionado(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorIss() != null) {
						dados.setValorIss(nfse.getInfNfse().getServico().getValores().getValorIss().doubleValue());
					} else {
						dados.setValorIss(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getOutrasRetencoes() != null) {
						dados.setValorOutrasRetencoes(
								nfse.getInfNfse().getServico().getValores().getOutrasRetencoes().doubleValue());
					} else {
						dados.setValorOutrasRetencoes(0.0);
					}
					dados.setTipoRetencao("" + nfse.getInfNfse().getServico().getValores().getIssRetido());
					if (nfse.getInfNfse().getServico().getValores().getValorCofins() != null) {
						dados.setValorCofins(
								nfse.getInfNfse().getServico().getValores().getValorCofins().doubleValue());
					} else {
						dados.setValorCofins(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorCsll() != null) {
						dados.setValorCsll(nfse.getInfNfse().getServico().getValores().getValorCsll().doubleValue());
					} else {
						dados.setValorCsll(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorDeducoes() != null) {
						dados.setValorDeducao(
								nfse.getInfNfse().getServico().getValores().getValorDeducoes().doubleValue());
					} else {
						dados.setValorDeducao(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorInss() != null) {
						dados.setValorInss(nfse.getInfNfse().getServico().getValores().getValorInss().doubleValue());
					} else {
						dados.setValorInss(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorIr() != null) {
						dados.setValorIr(nfse.getInfNfse().getServico().getValores().getValorIr().doubleValue());
					} else {
						dados.setValorIr(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorIssRetido() != null) {
						dados.setValorIssRetido(
								nfse.getInfNfse().getServico().getValores().getValorIssRetido().doubleValue());
					} else {
						dados.setValorIssRetido(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorServicos() != null) {
						dados.setValorTotalNfse(
								nfse.getInfNfse().getServico().getValores().getValorServicos().doubleValue());

					} else {
						dados.setValorTotalNfse(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorLiquidoNfse() != null) {
						dados.setValorLiquidoNfse(
								nfse.getInfNfse().getServico().getValores().getValorLiquidoNfse().doubleValue());
					}

					if (nfse.getInfNfse().getServico().getValores().getValorPis() != null) {
						dados.setValorPis(nfse.getInfNfse().getServico().getValores().getValorPis().doubleValue());
					} else {
						dados.setValorPis(0.0);
					}
					if (nfse.getInfNfse().getServico().getValores().getValorServicos() != null) {
						dados.setValorServico(
								nfse.getInfNfse().getServico().getValores().getValorServicos().doubleValue());
					} else {
						dados.setValorServico(0.0);
					}
				}

			}

			if (cancelamentoNfse != null) {

				dados.setNumeroPedidoCancelada(cancelamentoNfse.getConfirmacao().getPedido()
						.getInfPedidoCancelamento().getIdentificacaoNfse().getNumero().toString());
				dados.setCnpjCancelada(cancelamentoNfse.getConfirmacao().getPedido().getInfPedidoCancelamento()
						.getIdentificacaoNfse().getCnpj());
				dados.setInscricaoMunicipalCancelada(cancelamentoNfse.getConfirmacao().getPedido()
						.getInfPedidoCancelamento().getIdentificacaoNfse().getInscricaoMunicipal());
				dados.setCodigoMunicipioCancelada(Integer.toString(cancelamentoNfse.getConfirmacao().getPedido()
						.getInfPedidoCancelamento().getIdentificacaoNfse().getCodigoMunicipio()));
				dados.setCodigoCancelamento(cancelamentoNfse.getConfirmacao().getPedido().getInfPedidoCancelamento()
						.getCodigoCancelamento());
				dados.setNumeroCancelada(Integer.toString(cancelamentoNfse.getConfirmacao().getPedido()
						.getInfPedidoCancelamento().getIdentificacaoNfse().getNumero().intValue()));

				try {
					dados.setDataHoraCancelamento(
							XMLCalendarToString(cancelamentoNfse.getConfirmacao().getDataHoraCancelamento()));
					dados.setDataCancelamento(dados.getDataHoraCancelamento());
				} catch (Exception e) {
					System.out.println("Erro de data:" + cancelamentoNfse.getConfirmacao().getDataHoraCancelamento()
							+ " numero:" + dados.getNumeroCancelada() + " Prestador: " + dados.getCnpjCancelada());
				}
				
				dados.setMotivoCancelamento(dados.getOutrasInformacoes());
			}
			NotasMaeThread thread = new NotasMaeThread(dados, util, log);
			executor.execute(thread);
		} catch (Exception e) {
			e.printStackTrace();
			log.fillError(f.getName(), "Leitura de xml ", e);
		}

	}

	private String lerXML(File arquivo) {
		String linha = "";
		StringBuilder xml = new StringBuilder();
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
			while ((linha = in.readLine()) != null) {
				xml.append(linha);
			}
			in.close();
			return xml.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			log.fillError(f.getName(), "Leitura de xml ", e);
		}
		return "";
	}

	private boolean isNFSe(String xml) {
		return xml.contains("<Nfse") && xml.contains("<InfNfse");
	}

	private boolean isNfseCancelamento(String xml) {
		return xml.contains("<NfseCancelamento");
	}

	private String XMLCalendarToString(XMLGregorianCalendar xmlGregorianCalendar) {
		Calendar calendar = xmlGregorianCalendar.toGregorianCalendar();

		formatterData.setTimeZone(calendar.getTimeZone());
		String dateString = formatterData.format(calendar.getTime());
		return dateString;
	}
}
