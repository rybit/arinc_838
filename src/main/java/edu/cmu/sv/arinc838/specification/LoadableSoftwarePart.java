package edu.cmu.sv.arinc838.specification;

import java.math.BigInteger;

import com.arinc.arinc838.FileDefinitions;
import com.arinc.arinc838.LspIntegrityDefinition;
import com.arinc.arinc838.SdfIntegrityDefinition;
import com.arinc.arinc838.SoftwareDescription;
import com.arinc.arinc838.ThwDefinitions;

public interface LoadableSoftwarePart {
	
	 /**
     * Gets the value of the softwarePartnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoftwarePartnumber();

    /**
     * Sets the value of the softwarePartnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoftwarePartnumber(String value) ;

    /**
     * Gets the value of the softwareTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoftwareTypeDescription() ;

    /**
     * Sets the value of the softwareTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoftwareTypeDescription(String value) ;

    /**
     * Gets the value of the softwareTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public long getSoftwareTypeId() ;

    /**
     * Sets the value of the softwareTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSoftwareTypeId(long value) ;

	

	/**
	 * Sets the value of the softwareDescription property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link SoftwareDescription }
	 *     
	 */
	public void setSoftwareDescription(SoftwareDescription value);

	/**
	 * Gets the value of the thwDefinitions property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link ThwDefinitions }
	 *     
	 */
	public ThwDefinitions getThwDefinitions();

	/**
	 * Sets the value of the thwDefinitions property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link ThwDefinitions }
	 *     
	 */
	public void setThwDefinitions(ThwDefinitions value);

	/**
	 * Gets the value of the fileDefinitions property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link FileDefinitions }
	 *     
	 */
	public FileDefinitions getFileDefinitions();

	/**
	 * Sets the value of the fileDefinitions property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link FileDefinitions }
	 *     
	 */
	public void setFileDefinitions(FileDefinitions value);

	/**
	 * Gets the value of the sdfIntegrityDefinition property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link SdfIntegrityDefinition }
	 *     
	 */
	public SdfIntegrityDefinition getSdfIntegrityDefinition();

	/**
	 * Sets the value of the sdfIntegrityDefinition property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link SdfIntegrityDefinition }
	 *     
	 */
	public void setSdfIntegrityDefinition(SdfIntegrityDefinition value);

	/**
	 * Gets the value of the lspIntegrityDefinition property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link LspIntegrityDefinition }
	 *     
	 */
	public LspIntegrityDefinition getLspIntegrityDefinition();

	/**
	 * Sets the value of the lspIntegrityDefinition property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link LspIntegrityDefinition }
	 *     
	 */
	public void setLspIntegrityDefinition(LspIntegrityDefinition value);

}