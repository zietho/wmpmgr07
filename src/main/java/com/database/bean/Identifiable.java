/**
 * 
 */
package com.database.bean;

/**
 * @author Patrick
 *
 */
public interface Identifiable {
	/**
	 * @return the id
	 */
	public Integer getId();

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id);
	
	/**
	 * @return the version
	 */
	public Integer getVersion();

	/**
	 * @param version the id to version
	 */
	public void setVersion(Integer version);
}
