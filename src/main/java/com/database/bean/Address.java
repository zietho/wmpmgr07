package com.database.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="ADDRESS")
@AttributeOverride( name="id", column = @Column(name="id") )
@XmlRootElement
public class Address extends BaseBean{
	
	@Column(name="name")
	private String name;
	
	@Column(name="street")
	private String street;
	
	@Column(name="zip")
	private Integer zip;
	
	@Column(name="place")
	private String place;
	
	@Column(name="longitude")
	private Double longitude;
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="dateCreated")
	private Timestamp dateCreated;
	
	@ManyToMany(mappedBy="userAddresses", fetch=FetchType.EAGER)
    private Set<User> users = new HashSet<User>();

	@OneToMany(mappedBy="address", fetch=FetchType.EAGER)
    private Set<Sotivity> sotivities;
	
	/**
	 * 
	 */
	public Address() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the zip
	 */
	public Integer getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(Integer zip) {
		this.zip = zip;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	@XmlTransient
	public void setUsers(Set<User> users) {
		this.users = users;
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
	@XmlTransient
	public void setSotivities(Set<Sotivity> sotivities) {
		this.sotivities = sotivities;
	}
	
}
