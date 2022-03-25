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

import br.guia.sp.avioesguide.model.Administrador;
import br.guia.sp.avioesguide.repository.AdminRepository;

@Controller
public class AdminController {
	// repository para persistencia do administrador
	@Autowired
	private AdminRepository repository;

	@RequestMapping(value = "validacao", method = RequestMethod.GET) // url
	public String formVali() {
		return "admin/valiAdmin"; // pasta
	}

	@RequestMapping(value = "salvarAdministrador", method = RequestMethod.POST)
	public String salvarAdmin(@Valid Administrador admin, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			attr.addFlashAttribute("mensagemErro", "Verifique os campos...");
			return "redirect:validacao";
		}
		try {
			// salva o administrador
			repository.save(admin);
			attr.addFlashAttribute("mensagemSucesso", "Admistrador cadastrado com sucesso. ID: " + admin.getId());
			return "redirect:validacao";
		} catch (Exception e) {
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar o Administrador: " + e.getMessage());
		}
		return "redirect:validacao";
	}

	// request mapping para listar, informando a página desejada
	@RequestMapping("listarAdmin/{page}")
	public String listar(Model model, @PathVariable("page") int page) {
		// cria um pageble com 6 elementos por pagina, ordenando pelo nome, de forma
		// ascendente
		PageRequest pageble = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "nome"));
		// cria a pagina atual atrasves do repository
		Page<Administrador> pagina = repository.findAll(pageble);
		// descobrir o total de paginas
		int totalPages = pagina.getTotalPages();
		// cria uma lista de inteiros npara representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();
		// preencher a alista com as paginas
		for(int i = 0; i <totalPages; i++) {
			pageNumbers.add(i+1);
		}
		// adiciona as variaveis na Model
		model.addAttribute("admins", pagina.getContent());
		model.addAttribute("paginaAtual", page);
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("numPaginas", pageNumbers);
		// retorna para o HTML da lista
		return "admin/lista";
	}
	@RequestMapping("alterar")
	public String alterar(Model model, Long id) {
		Administrador adminis = repository.findById(id).get();
		model.addAttribute("admin", adminis);
		return "redirect:validacao";
	}
	@RequestMapping("excluir")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listarAdmin";
	}
}
