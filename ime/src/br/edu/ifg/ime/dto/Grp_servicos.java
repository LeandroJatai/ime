package br.edu.ifg.ime.dto;

public class Grp_servicos extends Dto {

	private String titulo = null;
	public Grp_servicos(int _id) {
		this.id = _id;
	}
	@Override
	public String toString() {
		return null;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
