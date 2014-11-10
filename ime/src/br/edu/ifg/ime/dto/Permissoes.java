package br.edu.ifg.ime.dto;

public class Permissoes extends Dto {

	private Usuario usuario;
	private Servico servico;
	private boolean liberado = false;
	private Perspectiva perspectiva;
	
	@Override
	public String toString() {
		return usuario.getId()+", "+servico.getServico();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public boolean getLiberado() {
		return liberado;
	}

	public void setLiberado(boolean liberado) {
		this.liberado = liberado;
	}

	public Perspectiva getPerspectiva() {
		return perspectiva;
	}

	public void setPerspectiva(Perspectiva perspectiva) {
		this.perspectiva = perspectiva;
	}

}
