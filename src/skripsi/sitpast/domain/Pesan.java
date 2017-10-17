package skripsi.sitpast.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Pesan implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3664586727976948824L;
	
	private Long id;
	private Date waktu;
	private String judul;
	private Mahasiswa fromMhs;
	private Mahasiswa toMhs;
	private Dosen fromDosen;
	private Dosen toDosen;
	private UserTable toUser;
	private UserTable fromUser;
	private String isi;
	private int status;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Column
	public Date getWaktu() {
		return waktu;
	}
	public void setWaktu(Date waktu) {
		this.waktu = waktu;
	}
	
	@Column
	public String getJudul() {
		return judul;
	}
	public void setJudul(String judul) {
		this.judul = judul;
	}
	
	@ManyToOne
	@JoinColumn(name="fromMhs")
	public Mahasiswa getFromMhs() {
		return fromMhs;
	}
	public void setFromMhs(Mahasiswa fromMhs) {
		this.fromMhs = fromMhs;
	}
	
	
	@ManyToOne
	@JoinColumn(name="toMhs")
	public Mahasiswa getToMhs() {
		return toMhs;
	}
	public void setToMhs(Mahasiswa toMhs) {
		this.toMhs = toMhs;
	}
	@ManyToOne
	@JoinColumn(name="fromDosen")
	public Dosen getFromDosen() {
		return fromDosen;
	}
	public void setFromDosen(Dosen fromDosen) {
		this.fromDosen = fromDosen;
	}
	
	@ManyToOne
	@JoinColumn(name="toDosen")
	public Dosen getToDosen() {
		return toDosen;
	}
	public void setToDosen(Dosen toDosen) {
		this.toDosen = toDosen;
	}
	
	@Column
	public String getIsi() {
		return isi;
	}
	public void setIsi(String isi) {
		this.isi = isi;
	}
	
	@Column
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserTable getToUser() {
		return toUser;
	}
	public void setToUser(UserTable toUser) {
		this.toUser = toUser;
	}
	public UserTable getFromUser() {
		return fromUser;
	}
	public void setFromUser(UserTable fromUser) {
		this.fromUser = fromUser;
	}
		
}
