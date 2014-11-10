package br.edu.ifg.ime.dto;

public class Texto extends Dto {

	private String texto = null;
	private Linguagem linguagem = null;
	private Chave_textual chave_textual = null;

	public Texto(int _id) {
		this.id = _id;
	}
	public Texto() {
		this.id = 0;
	}
	@Override
	public String toString() {
		return null;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Linguagem getLinguagem() {
		return linguagem;
	}
	public void setLinguagem(Linguagem linguagem) {
		this.linguagem = linguagem;
	}
	public Chave_textual getChave_textual() {
		return chave_textual;
	}
	public void setChave_textual(Chave_textual chave_textual) {
		this.chave_textual = chave_textual;
	}
}
