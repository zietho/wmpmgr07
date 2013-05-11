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
@Table(name = "CATEGORY")
@AttributeOverride( name="id", column = @Column(name="id") )
public class Category extends BaseBean{

	@Column(name = "userChoosable", columnDefinition = "BIT", length = 1)
	private boolean userChoosable;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(mappedBy="sotivityCategories", fetch=FetchType.EAGER)
    private Set<Sotivity> sotivities = new HashSet<Sotivity>();
	
	@ManyToMany(mappedBy="userCategories", fetch=FetchType.EAGER)
    private Set<User> users = new HashSet<User>();

	/**
	 * 
	 */
	public Category() {
		super();
	}

	/**
	 * @return the userChoosable
	 */
	public boolean isUserChoosable() {
		return userChoosable;
	}

	/**
	 * @param userChoosable the userChoosable to set
	 */
	public void setUserChoosable(boolean userChoosable) {
		this.userChoosable = userChoosable;
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

	/**
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
}
