package com.database.bean;

import java.sql.Timestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "THANKS")
@AttributeOverride( name="id", column = @Column(name="id") )
public class Thanks extends BaseBean{
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name="dateCreated")
	private Timestamp dateCreated;
	
	@ManyToOne
    @JoinColumn(name="user_id")
    private User user;
	
	@ManyToOne
    @JoinColumn(name="sotivity_id")
    private Sotivity sotivity;
	

	/**
	 * 
	 */
	public Thanks() {
		super();
	}


	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


	/**
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}


	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}


	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


	/**
	 * @return the sotivity
	 */
	public Sotivity getSotivity() {
		return sotivity;
	}


	/**
	 * @param sotivity the sotivity to set
	 */
	public void setSotivity(Sotivity sotivity) {
		this.sotivity = sotivity;
	}

}
