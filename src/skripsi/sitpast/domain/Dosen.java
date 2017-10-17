package skripsi.sitpast.domain;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name="dosen")
public class Dosen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8479030159176490532L;

	private Long id;
	private String nip;
	private String username;
	private String password;
	private String nama;
	private Prodi prodi;
	private String Alamat;
	private String noTelp;
	private String gelar;
	private String email;
	private List<Skripsi> dospemSatu = new ArrayList<Skripsi>();
	private List<Skripsi> dospemDua = new ArrayList<Skripsi>();
	private List<Skripsi> dosujiSatu = new ArrayList<Skripsi>();
	private List<Skripsi> dosujiDua = new ArrayList<Skripsi>();
	private List<DosenPKL> dosenPKL = new ArrayList<DosenPKL>();
	private List<PKL> dospemPKL = new ArrayList<PKL>();
	private List<BimbinganPKL> bimbinganPKL = new ArrayList<BimbinganPKL>();
	private List<Pesan> inbox = new ArrayList<Pesan>();
	private List<Pesan> outbox = new ArrayList<Pesan>();
	private List<BimbinganSkripsi> bimbinganSkripsi = new ArrayList<BimbinganSkripsi>();

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getNip() {
		return nip;
	}
	public void setNip(String nip) {
		this.nip = nip;
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
	
	@Column
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	@OneToMany(mappedBy="dosen",targetEntity=DosenPKL.class,fetch=FetchType.LAZY)
	public List<DosenPKL> getDosenPKL() {
		return dosenPKL;
	}
	public void setDosenPKL(List<DosenPKL> dosenPKL) {
		this.dosenPKL = dosenPKL;
	}

	
	
	@Column
	public String getGelar() {
		return gelar;
	}
	public void setGelar(String gelar) {
		this.gelar = gelar;
	}
	
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@ManyToOne(targetEntity=Prodi.class)
	@JoinColumn(name="id_prodi")
	public Prodi getProdi() {
		return prodi;
	}
	
	public void setProdi(Prodi prodi) {
		this.prodi = prodi;
	}
	
	@Column (name="alamat")
	public String getAlamat() {
		return Alamat;
	}
	public void setAlamat(String alamat) {
		Alamat = alamat;
	}
	
	@Column (name="no_telp")
	public String getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}
	
	
	@OneToMany(targetEntity=Pesan.class,mappedBy="toDosen",fetch=FetchType.LAZY)
	public List<Pesan> getInbox() {
		return inbox;
	}
	public void setInbox(List<Pesan> inbox) {
		this.inbox = inbox;
	}
	
	@OneToMany(targetEntity=Pesan.class,mappedBy="fromDosen",fetch=FetchType.LAZY)
	public List<Pesan> getOutbox() {
		return outbox;
	}
	public void setOutbox(List<Pesan> outbox) {
		this.outbox = outbox;
	}
	
	@OneToMany(mappedBy="dosPemSatu",fetch=FetchType.LAZY,targetEntity=Skripsi.class)
	public List<Skripsi> getDospemSatu() {
		return dospemSatu;
	}
	public void setDospemSatu(List<Skripsi> dospemSatu) {
		this.dospemSatu = dospemSatu;
	}
	
	@OneToMany(mappedBy="dosPemDua",fetch=FetchType.LAZY,targetEntity=Skripsi.class)
	public List<Skripsi> getDospemDua() {
		return dospemDua;
	}
	public void setDospemDua(List<Skripsi> dospemDua) {
		this.dospemDua = dospemDua;
	}
	
	@OneToMany(mappedBy="dosUjiSatu",fetch=FetchType.LAZY,targetEntity=Skripsi.class)
	public List<Skripsi> getDosujiSatu() {
		return dosujiSatu;
	}
	public void setDosujiSatu(List<Skripsi> dosujiSatu) {
		this.dosujiSatu = dosujiSatu;
	}
	
	@OneToMany(mappedBy="dosUjiDua",fetch=FetchType.LAZY,targetEntity=Skripsi.class)
	public List<Skripsi> getDosujiDua() {
		return dosujiDua;
	}
	public void setDosujiDua(List<Skripsi> dosujiDua) {
		this.dosujiDua = dosujiDua;
	}
	
	@OneToMany(mappedBy="dospem",fetch=FetchType.LAZY,targetEntity=BimbinganSkripsi.class)
	public List<BimbinganSkripsi> getBimbinganSkripsi() {
		return bimbinganSkripsi;
	}
	public void setBimbinganSkripsi(List<BimbinganSkripsi> bimbinganSkripsi) {
		this.bimbinganSkripsi = bimbinganSkripsi;
	}
	
	@OneToMany(mappedBy="dospem",fetch=FetchType.LAZY,targetEntity=BimbinganPKL.class)
	public List<BimbinganPKL> getBimbinganPKL() {
		return bimbinganPKL;
	}
	public void setBimbinganPKL(List<BimbinganPKL> bimbinganPKL) {
		this.bimbinganPKL = bimbinganPKL;
	}
	
	
	@OneToMany(mappedBy="dospemPKL",fetch=FetchType.LAZY,targetEntity=PKL.class)
	public List<PKL> getDospemPKL() {
		return dospemPKL;
	}
	public void setDospemPKL(List<PKL> dospemPKL) {
		this.dospemPKL = dospemPKL;
	}
	
	
	
	
}
