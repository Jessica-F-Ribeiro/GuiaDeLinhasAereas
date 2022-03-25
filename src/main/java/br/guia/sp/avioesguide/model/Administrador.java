package br.guia.sp.avioesguide.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.guia.sp.avioesguide.util.HashUtil;
import lombok.Data;

// para gerara o get e o set
@Data
// para mapear como uma entidade JPA
@Entity
public class Administrador {
	// chave primaria e auto-incremento
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	@Email
	@Column(unique = true)
	private String email;
	@NotEmpty
	private String senha;
	
	
	//metodo para setar a senha aplicando o hash
	public void setSenha(String senha) {
		// aplico o hash e seta a senha no objeto
		this.senha = HashUtil.hash256(senha);
	}
}
