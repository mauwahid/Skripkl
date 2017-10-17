package skripsi.sitpast.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="artikel")
public class Artikel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8479030159176490532L;

	private Long id;
	private Date tanggal;
	private String judul;
	private String penulis;
	private String isi;
	
	

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	
	@Column
	public String getJudul() {
		return judul;
	}
	public void setJudul(String judul) {
		this.judul = judul;
	}

	@Column
	public String getPenulis() {
		return penulis;
	}
	public void setPenulis(String penulis) {
		this.penulis = penulis;
	}
	
	
	@Column
	public String getIsi() {
		return isi;
	}
	public void setIsi(String isi) {
		this.isi = isi;
	}
	
	
}
