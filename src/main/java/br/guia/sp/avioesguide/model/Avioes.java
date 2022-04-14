package br.guia.sp.avioesguide.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Avioes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String cep; 
	private String rua;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	@Column(columnDefinition = "TEXT")
	private String fotos;
	@ManyToOne
	private TipoViagens tipo; //SELECT
	private boolean espacoInfantil;
	private boolean estacionamento;
	private boolean taxista;
	private boolean dinheiro;
	private boolean cartao;
	private boolean pix;
	private boolean boleto;
	private String site;
	private String telefone;
	
	public String[] verFotos() {
		return this.fotos.split(";");
	}
}
