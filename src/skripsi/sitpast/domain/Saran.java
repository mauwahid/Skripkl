package skripsi.sitpast.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Saran implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3664586727976948824L;
	
	private Long id;
	private String email;
	private String name;
	private Date date;
	private String saran;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column
	public String getSaran() {
		return saran;
	}
	public void setSaran(String saran) {
		this.saran = saran;
	}
	
	
	
}
