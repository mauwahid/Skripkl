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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="prodi")
public class Prodi implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8213900904494271596L;
	private Long id;
	private String namaProdi;
	private String keterangan;
	private List<UserTable> usersProdi = new ArrayList<UserTable>();
	private List<Mahasiswa> mahasiswas = new ArrayList<Mahasiswa>();
	private List<Dosen> dosens = new ArrayList<Dosen>();
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getNamaProdi() {
		return namaProdi;
	}
	public void setNamaProdi(String namaProdi) {
		this.namaProdi = namaProdi;
	}
	
	@Column
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	@OneToMany(mappedBy="prodi",cascade=CascadeType.ALL,fetch=FetchType.LAZY,targetEntity=Mahasiswa.class)
	public List<Mahasiswa> getMahasiswas() {
		return mahasiswas;
	}
	public void setMahasiswas(List<Mahasiswa> mahasiswas) {
		this.mahasiswas = mahasiswas;
	}
	
	@OneToMany(mappedBy="prodi",cascade=CascadeType.ALL,fetch=FetchType.LAZY,targetEntity=Dosen.class)
	public List<Dosen> getDosens() {
		return dosens;
	}
	public void setDosens(List<Dosen> dosens) {
		this.dosens = dosens;
	}
	
	@OneToMany(mappedBy="prodi",cascade=CascadeType.ALL,fetch=FetchType.LAZY,targetEntity=UserTable.class)
	public List<UserTable> getUsersProdi() {
		return usersProdi;
	}
	public void setUsersProdi(List<UserTable> usersProdi) {
		this.usersProdi = usersProdi;
	}
	
	
	
}
