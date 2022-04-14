package br.guia.sp.avioesguide.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.guia.sp.avioesguide.model.Administrador;

public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long>{
	public Administrador findByEmailAndSenha(String email, String senha);
}
