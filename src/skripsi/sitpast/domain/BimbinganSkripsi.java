package skripsi.sitpast.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="bimbingan_skripsi")
public class BimbinganSkripsi {

	private Long id;
	private Date tglBimbingan;
	private String catatan;
	private String tempat;
	private Skripsi skripsi;
	private String data;
	private Dosen dospem;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public Date getTglBimbingan() {
		return tglBimbingan;
	}
	public void setTglBimbingan(Date tglBimbingan) {
		this.tglBimbingan = tglBimbingan;
	}
	
	@Column
	public String getCatatan() {
		return catatan;
	}
	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	
	
	@Column
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	@ManyToOne
	@JoinColumn
	public Skripsi getSkripsi() {
		return skripsi;
	}
	public void setSkripsi(Skripsi skripsi) {
		this.skripsi = skripsi;
	}
	
	@Column
	public String getTempat() {
		return tempat;
	}
	public void setTempat(String tempat) {
		this.tempat = tempat;
	}
	
	@ManyToOne
	@JoinColumn
	public Dosen getDospem() {
		return dospem;
	}
	public void setDospem(Dosen dospem) {
		this.dospem = dospem;
	}
	
	
	
	
}
