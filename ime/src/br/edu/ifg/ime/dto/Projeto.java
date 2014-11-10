package br.edu.ifg.ime.dto;

import java.sql.Timestamp;

public class Projeto extends Dto {

	private String titulo = null;
	private String skin = null;
	private Usuario usuario = null;
	
	private Timestamp cadastrado = null;
	private Timestamp alterado = null;
	
	private boolean publico = false;
	
	private byte[] manifesto = null;
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return titulo;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Timestamp getCadastrado() {
		return cadastrado;
	}


	public void setCadastrado(Timestamp cadastrado) {
		this.cadastrado = cadastrado;
	}


	public Timestamp getAlterado() {
		return alterado;
	}


	public void setAlterado(Timestamp alterado) {
		this.alterado = alterado;
	}


	public boolean getPublico() {
		return publico;
	}


	public void setPublico(boolean publico) {
		this.publico = publico;
	}


	public byte[] getManifesto() {
		return manifesto;
	}


	public void setManifesto(byte[] manifesto) {
		this.manifesto = manifesto;
	}


	public String getSkin() {
		return skin;
	}


	public void setSkin(String skin) {
		this.skin = skin;
	}
	
	
	
	
}
