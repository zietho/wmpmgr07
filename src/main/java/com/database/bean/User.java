package com.database.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name="USER")
@AttributeOverride( name="id", column = @Column(name="id") )
@XmlRootElement
@Indexed
public class User extends BaseBean{
	
	@Column(name="moderator", columnDefinition = "BIT", length = 1)
	private boolean moderator;
	
	@Column(name="trusted", columnDefinition = "BIT", length = 1)
	private boolean trusted;
	
	@Column(name="nickname", unique = true)
	@Field
	private String nickname;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="tel")
	private String tel;
	
	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;
	
	@Column(name="picture")
	private String picture;
	
	@Column(name="dateCreated")
	private Timestamp dateCreated;
	
	@Column(name="salt")
	private String salt;
	
	@OneToMany(mappedBy="user" , fetch=FetchType.EAGER)
	private Set<Sotivity> ownedSotivities;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    private Set<Thanks> thanks;
	
	@OneToMany(mappedBy="user",fetch=FetchType.EAGER)
    private Set<Workflow> workflows;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(name="USER_ADDRESS",
                joinColumns={@JoinColumn(name="USER_ID")},
                inverseJoinColumns={@JoinColumn(name="ADDRESS_ID")})
    private Set<Address> userAddresses = new HashSet<Address>();
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(name="USER_CATEGORY",
                joinColumns={@JoinColumn(name="USER_ID")},
                inverseJoinColumns={@JoinColumn(name="CATEGORY_ID")})
	private Set<Category> userCategories = new HashSet<Category>();

	/**
	 * 
	 */
	public User() {
		super();
	}

	/**
	 * @return the moderator
	 */
	public boolean isModerator() {
		return moderator;
	}

	/**
	 * @param moderator the moderator to set
	 */
	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}

	/**
	 * @return the trusted
	 */
	public boolean isTrusted() {
		return trusted;
	}
	
	public String getTrustedLabel(){
		if(trusted){
			return "ja";
		}else{
			return "nein";
		}
	}

	/**
	 * @param trusted the trusted to set
	 */
	public void setTrusted(boolean trusted) {
		this.trusted = trusted;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	@XmlTransient
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
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
	@XmlTransient
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	@XmlTransient
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the ownedSotivities
	 */
	@XmlTransient
	public Set<Sotivity> getOwnedSotivities() {
		return ownedSotivities;
	}

	/**
	 * @param ownedSotivities the ownedSotivities to set
	 */
	public void setOwnedSotivities(Set<Sotivity> ownedSotivities) {
		this.ownedSotivities = ownedSotivities;
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
	 * @return the userAddresses
	 */
	public Set<Address> getUserAddresses() {
		return userAddresses;
	}

	public void addNewAddresses(List<Address> newAdds){
		this.userAddresses=new HashSet<Address>();
		this.userAddresses.addAll(newAdds);
	}
	
	/**
	 * @param userAddresses the userAddresses to set
	 */
	@XmlTransient
	public void setUserAddresses(Set<Address> userAddresses) {
		this.userAddresses = userAddresses;
	}
	
	

	/**
	 * @return the userCategories
	 */
	public Set<Category> getUserCategories() {
		return userCategories;
	}

	/**
	 * @param userCategories the userCategories to set
	 */
	@XmlTransient
	public void setUserCategories(Set<Category> userCategories) {
		this.userCategories = userCategories;
	}

	@Override
	public String toString() {
		return "User [moderator=" + moderator + ", trusted=" + trusted
				+ ", nickname=" + nickname + ", password=" + password
				+ ", email=" + email + ", tel=" + tel + ", firstname="
				+ firstname + ", lastname=" + lastname + ", picture=" + picture
				+ ", ownedSotivities=" + ownedSotivities + ", thanks=" + thanks
				+ ", workflows=" + workflows + ", userAddresses="
				+ userAddresses + "]";
	}
	
	
	

}
