package br.guia.sp.avioesguide.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.guia.sp.avioesguide.annotation.Publico;

@Component
public class AppInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// variavel para descobrir para onde estao tentando ir
		String uri = request.getRequestURI();
		// mostrar a URI
		System.out.println(uri);
		// verifica se o handler é um HandlerMethod, o que indica que foi encontrado um método em algum Controller para a requisicao
		if(handler instanceof HandlerMethod) {
			// liberar o acesso a pagina inicial
			if(uri.equals("/")) {
				return true;
			}
			if (uri.endsWith("/error")) {
				return true;
			}
			// fazer o casting para HandlerMethod
			HandlerMethod metodoChamado = (HandlerMethod) handler;
			// se o metodo for publico, libera
			if(metodoChamado.getMethodAnnotation(Publico.class) != null) {
				return true;
			}
			// verifica se existe um usuario logado
			if(request.getSession().getAttribute("usuarioLogado") != null) {
				return true;
			}else {
				// redirect para a página inicial
				response.sendRedirect("/");
				return false;
			}
		}
		return true;
	}
}
