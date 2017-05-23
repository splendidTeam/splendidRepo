/**
 * @author ruichao.gao
 * @
 *
 */
package com.baozun.nebula.model.offlineStore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_STORE_OFFLINESTORE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OfflineStore extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1404013180821884336L;
	
	private Long id;
	
	private String name;
	
	private String ename;
	
	private String fullAddress;
	
	private String ename1;
	
	private String ename2;
	
	private String province;
	
	private String city;
	
	private String district;
	
	private String address;
	
	private String extension;
	
	private String phone;
	
	private String hours;
	
	private String storeImage;
	
	private String mapImage;
	
    /** 邮编 */
    private String postcode;
    
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_STORE_OFFLINESTORE",sequenceName = "S_T_STORE_OFFLINESTORE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_STORE_OFFLINESTORE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
    @Index(name = "IDX_STORE_OFFLINESTORE_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ENAME")
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(name = "FULL_ADDRESS")
	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	@Column(name = "ENAME1")
	public String getEname1() {
		return ename1;
	}

	public void setEname1(String ename1) {
		this.ename1 = ename1;
	}

	@Column(name = "ENAME2")
	public String getEname2() {
		return ename2;
	}

	public void setEname2(String ename2) {
		this.ename2 = ename2;
	}

	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "DISTRICT")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "EXTENSION")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "HOURS")
	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}
	
	@Column(name = "STOREIMAGE")
	public String getStoreImage() {
		return storeImage;
	}

	public void setStoreImage(String storeImage) {
		this.storeImage = storeImage;
	}

	@Column(name = "MAPIMAGE")
	public String getMapImage() {
		return mapImage;
	}

	public void setMapImage(String mapImage) {
		this.mapImage = mapImage;
	}

	/**
	 * @return the postcode
	 */
	@Column(name = "POSTCODE")
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}




}