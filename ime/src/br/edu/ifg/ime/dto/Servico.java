package br.edu.ifg.ime.dto;

public class Servico extends Dto {

	private String descricao = null;
	private boolean ativo = false;
	private String servico = null;
	private boolean autenticar = false;
	private int tipo = 1;
	
	private Grp_servicos grupo = null;
	
	@Override
	public String toString() {
		return servico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public boolean getAutenticar() {
		return autenticar;
	}

	public void setAutenticar(boolean autenticar) {
		this.autenticar = autenticar;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Grp_servicos getGrupo() {
		return grupo;
	}

	public void setGrupo(Grp_servicos grupo) {
		this.grupo = grupo;
	}
}
