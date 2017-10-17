package skripsi.sitpast.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="mahasiswa")
public class Mahasiswa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6703540719120724913L;

	private Long id;
	private String nim;
	private String nama;
	private String alamat;
	private String angkatan;
	private String noTelp;
	private String email;
	private String username;
	private String password;
	private Prodi prodi;
	private Skripsi skripsi;
	private PKL pkl;
	private List<Pesan> inbox = new ArrayList<Pesan>();
	private List<Pesan> outbox = new ArrayList<Pesan>();
	
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getNim() {
		return nim;
	}
	public void setNim(String nim) {
		this.nim = nim;
	}
	
	@Column
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	@Column
	public String getAngkatan() {
		return angkatan;
	}
	public void setAngkatan(String angkatan) {
		this.angkatan = angkatan;
	}
	
	@ManyToOne(targetEntity=Prodi.class)
	@JoinColumn(name="id_prodi")
	public Prodi getProdi() {
		return prodi;
	}
	public void setProdi(Prodi prodi) {
		this.prodi = prodi;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "mahasiswa", cascade = CascadeType.ALL)
	public Skripsi getSkripsi() {
		return skripsi;
	}
	public void setSkripsi(Skripsi skripsi) {
		this.skripsi = skripsi;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "mahasiswa", cascade = CascadeType.ALL)
	public PKL getPkl() {
		return pkl;
	}
	public void setPkl(PKL pkl) {
		this.pkl = pkl;
	}
	
	@Column
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	@Column
	public String getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}
	
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(targetEntity=Pesan.class,mappedBy="toMhs")
	public List<Pesan> getInbox() {
		return inbox;
	}
	public void setInbox(List<Pesan> inbox) {
		this.inbox = inbox;
	}
	
	@OneToMany(targetEntity=Pesan.class,mappedBy="fromMhs")
	public List<Pesan> getOutbox() {
		return outbox;
	}
	public void setOutbox(List<Pesan> outbox) {
		this.outbox = outbox;
	}
}
