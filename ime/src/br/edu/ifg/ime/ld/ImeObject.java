package br.edu.ifg.ime.ld;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlTransient;
 
public abstract class ImeObject {

	final public static String ERROR = "ERROR:";
	final public static String WARNING = "WARNING:";
	
	
	@XmlTransient
	private static long idControl = 0;

	@XmlTransient
	private long id = 0;
	
	@XmlTransient
	private TreeMap<String, List<String>> validationMessages = null;

	public long getId() {
		
		if (id != 0)
			return id;
		
		id = newIdControl();
		return id;
	}
	
	private synchronized static long newIdControl() {
		return ++idControl;
	}

	protected TreeMap<String, List<String>> getStructureOfValidationMessages() {
		
		if (validationMessages != null)
			return validationMessages;
		
		validationMessages = new TreeMap<String, List<String>>();
		List<String> errors = new ArrayList<String>();
		List<String> warnings = new ArrayList<String>();
		validationMessages.put(ImeObject.ERROR, errors);
		validationMessages.put(ImeObject.WARNING, warnings);
		
		return validationMessages;
	}

	protected TreeMap<String, List<String>> clearStructureOfValidationMessages() {
		
		if (validationMessages == null)		
			return getStructureOfValidationMessages();
		
		validationMessages.get(ImeObject.ERROR).clear();
		validationMessages.get(ImeObject.WARNING).clear();
		
		return validationMessages;
	}

	protected void putERROR(String msg) {
		getStructureOfValidationMessages().get(ImeObject.ERROR).add(msg);
	}

	protected void putWARNING(String msg) {
		getStructureOfValidationMessages().get(ImeObject.WARNING).add(msg);
	}
	protected void putERRORs(List<String> msgs) {
		getStructureOfValidationMessages().get(ImeObject.ERROR).addAll(msgs);
	}

	protected void putWARNINGs(List<String> msgs) {
		getStructureOfValidationMessages().get(ImeObject.WARNING).addAll(msgs);
	}

	public List<String> getERRORs() {
		return getStructureOfValidationMessages().get(ImeObject.ERROR);
	}

	public List<String> getWARNINGs() {
		return getStructureOfValidationMessages().get(ImeObject.WARNING);
	}
	
	public abstract void validateImsLd() ;
	

}
