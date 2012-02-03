package edu.cmu.sv.arinc838.specification;

import com.arinc.arinc838.ThwPositions;

public interface TargetHardwareDefinition {

	/**
	 * Gets the value of the thwId property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getId();

	/**
	 * Sets the value of the thwId property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setId(String value);

	/**
	 * Gets the value of the thwPositions property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link ThwPositions }
	 *     
	 */
	public ThwPositions getPositions();

	/**
	 * Sets the value of the thwPositions property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link ThwPositions }
	 *     
	 */
	public void setPositions(ThwPositions value);

}