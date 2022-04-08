package br.guia.sp.avioesguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.guia.sp.avioesguide.model.Avioes;
import br.guia.sp.avioesguide.repository.AvioesRepository;
import br.guia.sp.avioesguide.repository.ViagensRepository;

@Controller
public class AvioesController {
	@Autowired
	private ViagensRepository repTipo;
	@Autowired
	private AvioesRepository repAv;
	@RequestMapping(value = "avioes", method = RequestMethod.GET)
	public String form(Model model){
		model.addAttribute("tipos", repTipo.findAll());
		return "avioes/form";
	}
	@RequestMapping("salvarAviao")
	public String salvarAvioes(Avioes avioes, @RequestParam("fileFotos") MultipartFile [] fileFotos) {
		System.out.println(fileFotos.length);
		//repAv.save(avioes);
		return "redirect:avioes";
	}
}
