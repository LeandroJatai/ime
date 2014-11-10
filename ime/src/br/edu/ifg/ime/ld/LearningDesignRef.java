package br.edu.ifg.ime.ld;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.imsglobal.jaxb.ld.LearningDesign;

import br.edu.ifg.ime.suport.LearningDesignUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "learning-design-ref")
public class LearningDesignRef implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 12345L;
	@XmlAttribute(required = true)
    @XmlIDREF
	private Object ref;

    public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}

	public LearningDesignRef() {
		
	}
	
}
