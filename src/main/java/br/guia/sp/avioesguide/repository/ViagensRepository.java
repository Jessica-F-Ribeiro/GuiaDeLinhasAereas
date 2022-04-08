package br.guia.sp.avioesguide.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.guia.sp.avioesguide.model.Avioes;
import br.guia.sp.avioesguide.model.TipoViagens;

public interface ViagensRepository extends PagingAndSortingRepository<TipoViagens, Long> {
	@Query("SELECT c FROM TipoViagens c WHERE c.palavrasChave LIKE %:p%")
	public List<TipoViagens> procurarPorTudo(@Param("p") String palavrasChave);
	
	public List<Avioes> findAllByOrderByNomeAsc();
}
