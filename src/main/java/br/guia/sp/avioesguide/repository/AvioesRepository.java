package br.guia.sp.avioesguide.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.guia.sp.avioesguide.model.Avioes;

public interface AvioesRepository extends PagingAndSortingRepository<Avioes, Long>{
	
}
