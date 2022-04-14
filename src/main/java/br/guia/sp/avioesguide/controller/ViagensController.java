package br.guia.sp.avioesguide.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.guia.sp.avioesguide.model.TipoViagens;
import br.guia.sp.avioesguide.repository.ViagensRepository;


@Controller
public class ViagensController {
	
	@Autowired
	private ViagensRepository repository;
	
	@RequestMapping("tiposViagem")
	private String formVi() {
		return "viagensList/formViagem";
	}
	@RequestMapping(value = "salvarTipos", method = RequestMethod.POST)
	private String salvarTipos(@Valid TipoViagens tipo, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			attr.addFlashAttribute("mensagemErro", "Verifique os campos...");
			return "redirect:tiposViagem";
		}
		try {
			repository.save(tipo);
			attr.addFlashAttribute("mensagemSucesso", "Admistrador cadastrado com sucesso. ID: " + tipo.getId());
			return "redirect:tiposViagem";
		} catch (Exception e) {
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar o Administrador: " + e.getMessage());
		}
		return "redirect:listarTipos/1";
	}
	
	@RequestMapping("listarTipos/{page}")
	public String listarTipos(Model model, @PathVariable("page") int page) {
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));
		Page<TipoViagens> pagina = repository.findAll(pageble);
		int totalPages = pagina.getTotalPages();
		List<Integer> pageNumbers = new ArrayList<Integer>();
		for(int i = 0; i <totalPages; i++) {
			pageNumbers.add(i+1);
		}
		model.addAttribute("tips", pagina.getContent());
		model.addAttribute("paginaAtual", page);
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("numPaginas", pageNumbers);
		return "viagensList/listaVi";
	}
	@RequestMapping("buscarPorPalavra")
	public String buscarPalavra(String palavrasChave, Model model, RedirectAttributes att) {
		model.addAttribute("tips", repository.procurarPorTudo("%"+palavrasChave+"%"));
		return "viagensList/listaVi";
	}
	@RequestMapping("alterarTipos")
	public String alterarTipos(Model model, Long id) {
		TipoViagens tipos = repository.findById(id).get();
		model.addAttribute("tips", tipos);
		return "forward:tiposViagem";
	}
	@RequestMapping("excluirTipos")
	public String excluirTipos(Long id) {
		repository.deleteById(id);
		return "redirect:listarTipos/1";
	}
}
