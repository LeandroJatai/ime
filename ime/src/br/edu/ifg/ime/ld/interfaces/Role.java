package br.edu.ifg.ime.ld.interfaces;

import org.imsglobal.jaxb.ld.ItemModel;
/**
 * 
 * @author leandro
 *
 * Interface de Agrupamento de partes comuns das Roles Leaner e Staff
 */
public interface Role {

	public ItemModel getInformation();
	public void setInformation(ItemModel value);
	public String getIdentifier();

}
