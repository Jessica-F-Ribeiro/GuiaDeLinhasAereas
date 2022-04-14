package br.guia.sp.avioesguide.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.guia.sp.avioesguide.model.Avioes;
import br.guia.sp.avioesguide.model.TipoViagens;
import br.guia.sp.avioesguide.repository.AvioesRepository;
import br.guia.sp.avioesguide.repository.ViagensRepository;
import br.guia.sp.avioesguide.util.FirebaseUtil;

@Controller
public class AvioesController {
	@Autowired
	private ViagensRepository repTipo;
	@Autowired
	private AvioesRepository repAv;
	@Autowired
	private FirebaseUtil firebase;

	@RequestMapping("avioes")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "avioes/form";
	}

	@RequestMapping(value = "salvarAviao", method = RequestMethod.POST)
	public String salvarAviao(@Valid Avioes avioes, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
		// string para URL das fotos
		String fotos = avioes.getFotos();
		// percorrer cada arquivo que foi submetido no formulario
		for (MultipartFile arquivo : fileFotos) {
			// verificar se o arquivo esta vazio
			if (arquivo.getOriginalFilename().isEmpty()) {
				// vai para o proximo arquivo
				continue;
			}
			// faz o upload para a nuvem e obtem a url gerada
			try {
				fotos += firebase.uploudFile(arquivo) + ";";
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		// atribui a string fotos ao objeto avioes
		avioes.setFotos(fotos);
		repAv.save(avioes);
		return "redirect:avioes";
	}

	@RequestMapping("listaAvioes/{page}")
	public String listarAvioes(Model model, @PathVariable("page") int page) {
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));
		Page<Avioes> pagina = repAv.findAll(pageble);
		int totalPages = pagina.getTotalPages();
		List<Integer> pageNumbers = new ArrayList<Integer>();
		for (int i = 0; i < totalPages; i++) {
			pageNumbers.add(i + 1);
		}
		model.addAttribute("avioesList", pagina.getContent());
		model.addAttribute("paginaAtual", page);
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("numPaginas", pageNumbers);
		return "avioes/listarAviao";
	}

	@RequestMapping("alteraAviao")
	public String alterarAvioes(Model model, Long id) {
		Avioes avioes = repAv.findById(id).get();
		model.addAttribute("avioesList", avioes);
		return "forward:avioes";
	}

	@RequestMapping("excluiAviao")
	public String excluirAv(Long id) {
		Avioes avi = repAv.findById(id).get();
		if (avi.getFotos().length() > 0) {

			for (String foto : avi.verFotos()) {
				firebase.deletar(foto);
			}
		}
		repAv.delete(avi);
		return "redirect:listaAviao/1";
	}

	@RequestMapping("excluirFotoAv")
	public String excluirFoto(Long idAvioes, int numFoto, Model model) {
		// busca o restaurante no banco de dados
		Avioes avi = repAv.findById(idAvioes).get();
		// pega a String da foto a ser excluida
		String fotoUrl = avi.verFotos()[numFoto];
		// excluir do firebase
		firebase.deletar(fotoUrl);
		// arranca a foto da String fotos
		avi.setFotos(avi.getFotos().replace(fotoUrl + ";", ""));
		// salva no BD o objeto avi
		repAv.save(avi);
		// adiciona o rest na model
		model.addAttribute("avioesList", avi);
		// encaminhar para o fom
		return "forward:avioes";
	}
}
