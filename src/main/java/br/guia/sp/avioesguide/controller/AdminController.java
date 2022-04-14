package br.guia.sp.avioesguide.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
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

import br.guia.sp.avioesguide.annotation.Publico;
import br.guia.sp.avioesguide.model.Administrador;
import br.guia.sp.avioesguide.repository.AdminRepository;
import br.guia.sp.avioesguide.util.HashUtil;

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
		// verifica se esta sendo feita uma alteraçao ao inves de uma insercao
		boolean alteracao = admin.getId() != null ? true : false;
		
		// verifica se a senha esta vazia
		if(admin.getSenha().equals(HashUtil.hash256(""))) {
			// se nao for alteracao, eu defino a primeira parte do e-mail como a senha
			if(!alteracao) {
				// extrai a parte do email antes do @
				String parte = admin.getEmail().substring(0, admin.getEmail().indexOf("@"));
				// define a senha do admin
				admin.setSenha(parte);
			}else {
				// busca a senha atual
				String hash = repository.findById(admin.getId()).get().getSenha();
				// seta a sneha som hash
				admin.setSenhaComHash(hash);
			}
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
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));
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
		return "forward:validacao";
	}
	@Publico
	@RequestMapping("login")
	private String login(Administrador admLogin, RedirectAttributes attr, HttpSession session) {
		// buscar o adm no BD atraves do email e senha
		Administrador admin = repository.findByEmailAndSenha(admLogin.getEmail(), admLogin.getSenha());
		// verifica se existe o admin
		if(admin == null) {
			// se não for nulo, avisa ao usuario
			attr.addFlashAttribute("mensagemErro", "Login e/ou senha inválido(s)");
			return "redirect:/";
		}else {
			// se não for nulo, salva na sessao e acessa o sistema
			session.setAttribute("usuarioLogado", admin);
			return "redirect:listaAvioes/1";
		}
	}
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		// elimina o ususario da session
		session.invalidate();
		// retorna para a página inicial
		return "redirect:login";
	}
	
	@RequestMapping("excluir")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listarAdmin/";
	}
}
