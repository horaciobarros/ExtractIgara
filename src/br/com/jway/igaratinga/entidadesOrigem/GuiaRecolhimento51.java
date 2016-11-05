package br.com.jway.igaratinga.entidadesOrigem;

public class GuiaRecolhimento51 {
	
	private String idGuiaRecolhimento;
	private String inscricaoMunicipalContribuinte;
	private String numGuia;
	private String parcela;
	private String conta;
	private String dataEmissao;
	private String dataVencimento;
	private String dataValidade;
	private String valor;
	private String mesCompetencia;
	private String anoCompetencia;
	
	
	
	public GuiaRecolhimento51(String idGuiaRecolhimento, String inscricaoMunicipalContribuinte, String numGuia,
			String parcela,String conta, String dataEmissao, String dataVencimento,
			String dataValidade, String valor, String mesCompetencia, String anoCompetencia) {
		super();
		this.idGuiaRecolhimento = idGuiaRecolhimento;
		this.inscricaoMunicipalContribuinte = inscricaoMunicipalContribuinte;
		this.numGuia = numGuia;
		this.parcela = parcela;
		this.conta = conta;
		this.dataEmissao = dataEmissao;
		this.dataVencimento = dataVencimento;
		this.dataValidade = dataValidade;
		this.valor = valor;
		this.mesCompetencia = mesCompetencia;
		this.anoCompetencia = anoCompetencia;
	}
	public String getIdGuiaRecolhimento() {
		return idGuiaRecolhimento;
	}
	public void setIdGuiaRecolhimento(String idGuiaRecolhimento) {
		this.idGuiaRecolhimento = idGuiaRecolhimento;
	}
	public String getInscricaoMunicipalContribuinte() {
		return inscricaoMunicipalContribuinte;
	}
	public void setInscricaoMunicipalContribuinte(String inscricaoMunicipalContribuinte) {
		this.inscricaoMunicipalContribuinte = inscricaoMunicipalContribuinte;
	}
	public String getNumGuia() {
		return numGuia;
	}
	public void setNumGuia(String numGuia) {
		this.numGuia = numGuia;
	}
	public String getParcela() {
		return parcela;
	}
	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	public String getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getDataValidade() {
		return dataValidade;
	}
	public void setDataValidade(String dataValidade) {
		this.dataValidade = dataValidade;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getMesCompetencia() {
		return mesCompetencia;
	}
	public void setMesCompetencia(String mesCompetencia) {
		this.mesCompetencia = mesCompetencia;
	}
	public String getAnoCompetencia() {
		return anoCompetencia;
	}
	public void setAnoCompetencia(String anoCompetencia) {
		this.anoCompetencia = anoCompetencia;
	}
	
	

}
