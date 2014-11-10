package br.edu.ifg.ime.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.edu.ifg.ime.dto.Usuario;
//LinguagemController
public class Lc extends LinguagemController{


	public static String getTexto(HttpSession session, String chave) {
		Usuario usuario = UsuarioController.getUsuarioConectado(session);
		return usuario.getTexto(chave);

	}
	public static String getTexto(HttpServletRequest request, String chave) {
		Usuario usuario = UsuarioController.getUsuarioConectado(request.getSession());
		return usuario.getTexto(chave);
	}
}
