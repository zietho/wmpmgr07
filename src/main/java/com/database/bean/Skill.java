package com.database.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SKILL")
@AttributeOverride( name="id", column = @Column(name="id") )
public class Skill extends BaseBean{
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(mappedBy="sotivitySkills", fetch=FetchType.EAGER)
    private Set<Sotivity> sotivities = new HashSet<Sotivity>();

	/**
	 * 
	 */
	public Skill() {
		super();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the sotivities
	 */
	public Set<Sotivity> getSotivities() {
		return sotivities;
	}

	/**
	 * @param sotivities the sotivities to set
	 */
	public void setSotivities(Set<Sotivity> sotivities) {
		this.sotivities = sotivities;
	}

	
	
}
