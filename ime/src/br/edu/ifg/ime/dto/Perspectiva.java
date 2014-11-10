package br.edu.ifg.ime.dto;

public class Perspectiva extends Dto {

	private String titulo = null;
	private Usuario usuario;
	private Linguagem linguagem = null;

	public Perspectiva(int _id) {
		this.id = _id;
	}
	public Perspectiva() {
		this.id = 0;
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
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Linguagem getLinguagem() {
		return linguagem;
	}
	public void setLinguagem(Linguagem linguagem) {
		this.linguagem = linguagem;
	}

}
