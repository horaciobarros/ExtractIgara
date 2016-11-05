package br.com.jway.igaratinga.entidadesOrigem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "atv_economica")
public class AtividadeEconomicaContribuinte {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String inscricaoMunicipal;
	private String cNAE2_0;
	private String descricao;
	private String indicaPrincipal;
	
	
	
	public AtividadeEconomicaContribuinte(String inscricaoMunicipal, String cNAE2_0, String descricao,
			String indicaPrincipal) {
		super();
		this.inscricaoMunicipal = inscricaoMunicipal;
		this.cNAE2_0 = cNAE2_0;
		this.descricao = descricao;
		this.indicaPrincipal = indicaPrincipal;
	}
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	public String getcNAE2_0() {
		return cNAE2_0;
	}
	public void setcNAE2_0(String cNAE2_0) {
		this.cNAE2_0 = cNAE2_0;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getIndicaPrincipal() {
		return indicaPrincipal;
	}
	public void setIndicaPrincipal(String indicaPrincipal) {
		this.indicaPrincipal = indicaPrincipal;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

}
