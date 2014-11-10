package br.edu.ifg.ime.dto;

import java.io.Serializable;


/**
 *
 * @author Leandro
 */
@SuppressWarnings("serial")
public abstract class Dto implements Serializable, Comparable<Dto> {

	protected int id = 0;

	public String msg = "";

	public int wmx_ordem = 0; // informação tranziente

	public Dto(int id) {
		this.id = id;
	}

	public Dto() {

	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public abstract String toString();

	public int compareTo(Dto o) {
		return this.id == o.id ? 0 : 
			(this.wmx_ordem == o.wmx_ordem ? (this.id > o.id?1:-1) : (this.wmx_ordem > o.wmx_ordem?1:-1));   	
	}

	public boolean equals(Dto o) {
		return id == o.id;
	}
}
