package com.database.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * @author Patrick
 *
 */
@Entity
@Table(name = "SOTIVITY")
@AttributeOverride( name="id", column = @Column(name="id") )
@XmlRootElement
@Indexed
public class Sotivity extends BaseBean{
	
	@Field
	@Column(name = "publicVisible", columnDefinition = "BIT", length = 1)
	private boolean publicVisible;
	
	@Column(name = "groupSotivity", columnDefinition = "BIT", length = 1)
	private boolean groupSotivity;
	
	@Column(name = "done", columnDefinition = "BIT", length = 1)
	private boolean done;
	
	@Column(name = "title")
	@Field
	private String title;
	
	@Column(name = "description")
	@Field
	private String description;
	
	@Column(name = "skills")
	@Field
	private String skills;
	
	@Column(name = "duration")
	@Field
	private Integer duration;
	
	@Column(name="dateCreated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@Column(name="dateEnd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEnd;
	
	@Column(name="facebookId")
	@Field
	private String facebookId;
	
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@ManyToOne
    @JoinColumn(name="user_id")
	@IndexedEmbedded
    private User user;
	
	@ManyToOne
    @JoinColumn(name="address_id")
    private Address address;
	
	@OneToMany(mappedBy="sotivity", fetch=FetchType.EAGER)
    private Set<Workflow> workflows;
	
	@OneToMany(mappedBy="sotivity", fetch=FetchType.EAGER)
    private Set<Thanks> thanks;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(name="SOTIVITY_CATEGORY",
                joinColumns={@JoinColumn(name="SOTIVITY_ID")},
                inverseJoinColumns={@JoinColumn(name="CATEGORY_ID")})
	private Set<Category> sotivityCategories = new HashSet<Category>();
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(name="SOTIVITY_SKILL",
                joinColumns={@JoinColumn(name="SOTIVITY_ID")},
                inverseJoinColumns={@JoinColumn(name="SKILL_ID")})
	private Set<Skill> sotivitySkills = new HashSet<Skill>();
	
	/**
	 * 
	 */
	public Sotivity() {
		super();
	}

	/**
	 * @return the publicVisible
	 */
	public boolean isPublicVisible() {
		return publicVisible;
	}

	/**
	 * @param publicVisible the publicVisible to set
	 */
	public void setPublicVisible(boolean publicVisible) {
		this.publicVisible = publicVisible;
	}

	/**
	 * @return the groupSotivity
	 */
	public boolean isGroupSotivity() {
		return groupSotivity;
	}

	/**
	 * @param groupSotivity the groupSotivity to set
	 */
	public void setGroupSotivity(boolean groupSotivity) {
		this.groupSotivity = groupSotivity;
	}

	/**
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	public void setDone(boolean done) {
		this.done = done;
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
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the dateEnd
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
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
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	@XmlTransient
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the workflows
	 */
	public Set<Workflow> getWorkflows() {
		return workflows;
	}

	/**
	 * @param workflows the workflows to set
	 */
	@XmlTransient
	public void setWorkflows(Set<Workflow> workflows) {
		this.workflows = workflows;
	}

	/**
	 * @return the thanks
	 */
	public Set<Thanks> getThanks() {
		return thanks;
	}

	/**
	 * @param thanks the thanks to set
	 */
	@XmlTransient
	public void setThanks(Set<Thanks> thanks) {
		this.thanks = thanks;
	}

	/**
	 * @return the sotivityCategories
	 */
	public Set<Category> getSotivityCategories() {
		return sotivityCategories;
	}

	/**
	 * @param sotivityCategories the sotivityCategories to set
	 */
	@XmlTransient
	public void setSotivityCategories(Set<Category> sotivityCategories) {
		this.sotivityCategories = sotivityCategories;
	}
	
	/**
	 * @return the sotivitySkills
	 */
	public Set<Skill> getSotivitySkills() {
		return sotivitySkills;
	}

	/**
	 * @param sotivitySkills the sotivitySkills to set
	 */
	@XmlTransient
	public void setSotivitySkills(Set<Skill> sotivitySkills) {
		this.sotivitySkills = sotivitySkills;
	}

	/**
	 * @return the skills
	 */
	public String getSkills() {
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(String skills) {
		this.skills = skills;
	}

	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	
}
