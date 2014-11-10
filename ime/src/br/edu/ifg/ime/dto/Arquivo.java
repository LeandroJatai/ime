package br.edu.ifg.ime.dto;

import java.sql.Timestamp;

public class Arquivo extends Dto {

	private String nome = null;
	
	private String titulo = null;

	private String content_type = null;
	
	private Timestamp cadastrado = null;
	private Timestamp alterado = null;
	
	private byte[] arquivo = null;
	
	private int unidade = 1;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nome;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public int getUnidade() {
		return unidade;
	}

	public void setUnidade(int unidade) {
		this.unidade = unidade;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
	
}
