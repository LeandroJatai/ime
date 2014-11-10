/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifg.ime.dto;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import br.edu.ifg.ime.controllers.LinguagemController;
import br.edu.ifg.ime.dao.ServicoDAO;

/**
 *
 * @author Leandro
 */

public class Usuario extends Dto {

	public static final int wmx_convidado = 1000;
	public static final int wmx_root = 1;

	public GregorianCalendar wmx_cache_permissoes = null; 

	private String nome = null;
	private String senha = null;
	private String login = null;
	private boolean ativo = true;
	private String tipo_perfil = "perfil.livre";
	private Usuario perfil = null;

	private List<Permissoes> l_permissoes = null;
	private List<Perspectiva> l_perspectivas = null;

	public Perspectiva wmx_perspectiva = null;

	private TreeMap<String, String> l_textos = null;
	private Linguagem linguagem = null;

	public Usuario() {
	}

	public Usuario(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public String toString() {
		return nome;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public List<Permissoes> getL_permissoes() {

		if (l_permissoes == null)
			l_permissoes = new ArrayList<Permissoes>();

		return l_permissoes;
	}

	public void setL_permissoes(List<Permissoes> l_permissoes) {
		this.l_permissoes = l_permissoes;
	}

	public boolean hasPermissoes(String action) {


		for (Permissoes p: getL_permissoes()) {			
			if (p.getServico().getServico().equals(action) && p.getLiberado())
				return true;
		}
		return false;
	}

	public List<Perspectiva> getL_perspectivas() {
		if (l_perspectivas == null)
			l_perspectivas = new ArrayList<Perspectiva>();

		return l_perspectivas;
	}

	public void setL_perspectivas(List<Perspectiva> l_perspectivas) {
		this.l_perspectivas = l_perspectivas;
	}

	public Usuario getPerfil() {
		return perfil;
	}

	public void setPerfil(Usuario perfil) {
		this.perfil = perfil;
	}

	public String getTipo_perfil() {
		return tipo_perfil;
	}

	public void setTipo_perfil(String tipo_perfil) {
		this.tipo_perfil = tipo_perfil;
	}

	public boolean cacheValido() {
		if (wmx_cache_permissoes == null)
			return false;

		GregorianCalendar g = new GregorianCalendar();

		if (g.getTimeInMillis() - wmx_cache_permissoes.getTimeInMillis() < 300000)
			return true;
		else
			return false;
	}
	public void validarCache() {

		wmx_cache_permissoes = new GregorianCalendar();

	}



	public String getTexto(String chave, String tipoChave) {

		if (tipoChave == LinguagemController.chaveTl) {
			return getTexto(chave);
		}
		else if (tipoChave == LinguagemController.chaveDb) {
			if (chave.startsWith(LinguagemController.chaveDb)) {
				return getTexto(chave.substring(LinguagemController.chaveDb.length()));
			}
		}
		return chave;


	}		
	public String getTexto(String chave) { //Oficial

		if (chave == null)
			return "";

		if (!LinguagemController.isValidCache()) {
			l_textos = null;
		}

		if (l_textos == null) {
			l_textos = LinguagemController.getTextosUsuario(this);
		}

		String texto = l_textos.get(chave); 
		if (texto == null)
			texto = LinguagemController.getTextoPadrao(chave);
		
		//texto = "*"+texto;
		return texto;

	}
	public String getTextoDev(String chave) { //dev

		if (chave == null)
			return "";
 
		String texto = null; 
		if (texto == null)
			texto = LinguagemController.getTextoPadrao(chave);

		texto = "*"+texto;

		return texto;

	}

	public Linguagem getLinguagem() {
		return linguagem;
	}

	public void setLinguagem(Linguagem linguagem) {
		this.linguagem = linguagem;
	}

	public void reloadPerfilTextual() {
		l_textos = LinguagemController.getTextosUsuario(this);

	}


}
