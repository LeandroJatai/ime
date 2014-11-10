package br.edu.ifg.ime.dto;

public class Chave_textual extends Dto {

	/*
	 * tipos:
	 * 1 - display tela
	 * 2 - display bd
	 * 3 - json
	 * 4 - xsd
	 *  
	 */
	
	
	private String chave = null;
	private String texto = null;
	
	private int tipo = 1;

	public Chave_textual(int _id) {
		this.id = _id;
	}
	public Chave_textual() {
		this.id = 0;
	}
	@Override
	public String toString() {
		return chave;
	}
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}
