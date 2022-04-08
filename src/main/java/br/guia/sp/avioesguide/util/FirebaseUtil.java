package br.guia.sp.avioesguide.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class FirebaseUtil {
	// variavel para gravar as credenciais do Firebase
	private Credentials credenciais;
	// variavel para acessar o storage
	private Storage storage;
	// constante para o nome do bucket
	private final String BUCKET_NAME = "avioesguide.appspot.com";
	// constante para o prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/"+BUCKET_NAME+"/o/";
	// constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	// constante para a URL
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;
	
	public FirebaseUtil() {
		// busca as credenciais (arquivo JSON)
		Resource resource = new ClassPathResource("firebase-chave.json");
		// ler o arquivo para obter as credenciais
		try {
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			// acessa o serviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public String uploudFile(MultipartFile arquivo) {
		// gera uma String aleatoria para o nome do arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		return "nomeArquivo";
	}
	// retorna a extensao de um arquivo atraves do seu nome
	private String getExtensao(String nomeArquivo) {
		// retorna o trecho da string que vai do ultimo ponto ate o fim
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
}
