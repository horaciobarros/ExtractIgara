package br.com.jway.igaratinga.entidadesOrigem;

public class GuiasNFSe {
	private String idRegistro;
	private String tipo;
	private String inscricaoMunicipal;
	private String numeroDocumento;
	private String codigoVerificacao;
	
	public GuiasNFSe(String idRegistro, String tipo, String inscricaoMunicipal, String numeroDocumento,
			String codigoVerificacao, String idLancamento, String idGuiaRecolhimento) {
		super();
		this.idRegistro = idRegistro;
		this.tipo = tipo;
		this.inscricaoMunicipal = inscricaoMunicipal;
		this.numeroDocumento = numeroDocumento;
		this.codigoVerificacao = codigoVerificacao;
		this.idLancamento = idLancamento;
		this.idGuiaRecolhimento = idGuiaRecolhimento;
	}
	public String getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getCodigoVerificacao() {
		return codigoVerificacao;
	}
	public void setCodigoVerificacao(String codigoVerificacao) {
		this.codigoVerificacao = codigoVerificacao;
	}
	public String getIdLancamento() {
		return idLancamento;
	}
	public void setIdLancamento(String idLancamento) {
		this.idLancamento = idLancamento;
	}
	public String getIdGuiaRecolhimento() {
		return idGuiaRecolhimento;
	}
	public void setIdGuiaRecolhimento(String idGuiaRecolhimento) {
		this.idGuiaRecolhimento = idGuiaRecolhimento;
	}
	private String idLancamento;
	private String idGuiaRecolhimento;
	
	

}
