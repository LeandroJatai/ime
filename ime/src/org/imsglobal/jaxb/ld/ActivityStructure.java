//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.08 at 10:43:46 AM BRST 
//


package org.imsglobal.jaxb.ld;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.edu.ifg.ime.ld.ImeObject;


/**
 * <p>Java class for activity-structureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="activity-structureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}title" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}information" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}environment-ref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}learning-activity-ref"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}support-activity-ref"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}unit-of-learning-href"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}activity-structure-ref"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsld_v1p0}metadata" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsld_v1p0}attr.number-to-select"/>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsld_v1p0}attr.identifier.req"/>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsld_v1p0}attr.structure-type"/>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsld_v1p0}attr.sort"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activity-structureType", propOrder = {
    "title",
    "information",
    "environmentRef",
    "learningActivityRefOrSupportActivityRefOrUnitOfLearningHref",
    "metadata"
})
public class ActivityStructure extends ImeObject implements Serializable
{

    private final static long serialVersionUID = 12345L;
    protected String title;
    protected ItemModel information;
    @XmlElement(name = "environment-ref")
    protected List<EnvironmentRef> environmentRef;
    @XmlElements({
        @XmlElement(name = "activity-structure-ref", type = ActivityStructureRef.class),
        @XmlElement(name = "learning-activity-ref", type = LearningActivityRef.class),
        @XmlElement(name = "support-activity-ref", type = SupportActivityRef.class),
        @XmlElement(name = "unit-of-learning-href", type = UnitOfLearningHref.class)
    })
    protected List<Serializable> learningActivityRefOrSupportActivityRefOrUnitOfLearningHref;
    protected Extend metadata;
    @XmlAttribute(name = "number-to-select")
    protected BigInteger numberToSelect;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String identifier;
    @XmlAttribute(name = "structure-type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String structureType;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String sort;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the information property.
     * 
     * @return
     *     possible object is
     *     {@link ItemModel }
     *     
     */
    public ItemModel getInformation() {
        return information;
    }

    /**
     * Sets the value of the information property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemModel }
     *     
     */
    public void setInformation(ItemModel value) {
        this.information = value;
    }

    /**
     * Gets the value of the environmentRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environmentRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironmentRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvironmentRef }
     * 
     * 
     */
    public List<EnvironmentRef> getEnvironmentRef() {
        if (environmentRef == null) {
            environmentRef = new ArrayList<EnvironmentRef>();
        }
        return this.environmentRef;
    }

    /**
     * Gets the value of the learningActivityRefOrSupportActivityRefOrUnitOfLearningHref property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningActivityRefOrSupportActivityRefOrUnitOfLearningHref property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActivityStructureRef }
     * {@link LearningActivityRef }
     * {@link SupportActivityRef }
     * {@link UnitOfLearningHref }
     * 
     * 
     */
    public List<Serializable> getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref() {
        if (learningActivityRefOrSupportActivityRefOrUnitOfLearningHref == null) {
            learningActivityRefOrSupportActivityRefOrUnitOfLearningHref = new ArrayList<Serializable>();
        }
        return this.learningActivityRefOrSupportActivityRefOrUnitOfLearningHref;
    }

    /**
     * Gets the value of the metadata property.
     * 
     * @return
     *     possible object is
     *     {@link Extend }
     *     
     */
    public Extend getMetadata() {
        return metadata;
    }

    /**
     * Sets the value of the metadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Extend }
     *     
     */
    public void setMetadata(Extend value) {
        this.metadata = value;
    }

    /**
     * Gets the value of the numberToSelect property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberToSelect() {
        return numberToSelect;
    }

    /**
     * Sets the value of the numberToSelect property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberToSelect(BigInteger value) {
        this.numberToSelect = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the structureType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStructureType() {
        return structureType;
    }

    /**
     * Sets the value of the structureType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStructureType(String value) {
        this.structureType = value;
    }

    /**
     * Gets the value of the sort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSort() {
        if (sort == null) {
            return "as-is";
        } else {
            return sort;
        }
    }

    /**
     * Sets the value of the sort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSort(String value) {
        this.sort = value;
    }

	@Override
	public void validateImsLd() {

		clearStructureOfValidationMessages();

		if (title == null || title.length() == 0)
			putWARNING("*Atividade sem título!");
		 

		if (information != null && information.isEmpty()) {
			information = null;
		} else if (information != null) {
			information.validateImsLd();
			putERRORs(information.getERRORs());
			putWARNINGs(information.getWARNINGs());
		}
		
		if (getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().size() == 0)
			putERROR("*Deve existir ao menos uma atividade vinculada a estrutura de atividade:" + title);
		 

	}

}