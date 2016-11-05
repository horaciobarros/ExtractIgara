package br.com.jway.igaratinga.entidadesOrigem;

public class GuiaRecolhimento53 {
	
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
	private String dataSuspensao;
	private String motivo;
	private String usuario;
	
	
	
	public GuiaRecolhimento53(String idGuiaRecolhimento, String inscricaoMunicipalContribuinte, String numGuia,
			String parcela, String conta, String dataEmissao, String dataVencimento, String dataValidade, String valor,
			String mesCompetencia, String anoCompetencia, String dataSuspensao, String motivo, String usuario) {
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
		this.dataSuspensao = dataSuspensao;
		this.motivo = motivo;
		this.usuario = usuario;
	}
	public String getIdGuiaRecolhimento() {
		return idGuiaRecolhimento;
	}
	public String getInscricaoMunicipalContribuinte() {
		return inscricaoMunicipalContribuinte;
	}
	public String getNumGuia() {
		return numGuia;
	}
	public String getParcela() {
		return parcela;
	}
	public String getConta() {
		return conta;
	}
	public String getDataEmissao() {
		return dataEmissao;
	}
	public String getDataVencimento() {
		return dataVencimento;
	}
	public String getDataValidade() {
		return dataValidade;
	}
	public String getValor() {
		return valor;
	}
	public String getMesCompetencia() {
		return mesCompetencia;
	}
	public String getAnoCompetencia() {
		return anoCompetencia;
	}
	public String getDataSuspensao() {
		return dataSuspensao;
	}
	public String getMotivo() {
		return motivo;
	}
	public String getUsuario() {
		return usuario;
	}
	
	
	
}
