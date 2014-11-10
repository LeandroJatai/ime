package br.edu.ifg.ime.dto;

public class Linguagem extends Dto {

	private String titulo = null;

	public Linguagem(int _id) {
		this.id = _id;
	}
	public Linguagem() {
		this.id = 0;
	}
	@Override
	public String toString() {
		return titulo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
