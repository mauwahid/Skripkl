package skripsi.sitpast.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="PKL")//java anotations=penanda atribut
public class PKL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2076836678098530019L;
	private Long id;
	private Mahasiswa mahasiswa;
	private String judul;
	private Date tanggalMulai;
	private Date tanggalAkhir;
	private String tempatPKL;
	private int status;
	
	private String dataAwal;
	private String dataBab1;
	private String dataBab2;
	private String dataBab3;
	private String dataBab4;
	private String dataBab5;
	private String daftarPustaka;
	private String lampiran;
	private Dosen dospemPKL;
	private int disiplin;
	private int kemampuan;
	private int kerapihan;
	private int nilaiTotal;
	
	
	
	
	 @Id
	 @Column(name="id", unique=true, nullable=false)
	 @GeneratedValue(generator="gen")
	 @GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(name="property", value="mahasiswa"))
	public Long getId() {
		return id;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	
	@Column
	public String getJudul() {
		return judul;
	}

	public void setJudul(String judul) {
		this.judul = judul;
	}

	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Column
	public Date getTanggalMulai() {
		return tanggalMulai;
	}
	public void setTanggalMulai(Date tanggalMulai) {
		this.tanggalMulai = tanggalMulai;
	}
	
	@Column
	public Date getTanggalAkhir() {
		return tanggalAkhir;
	}
	public void setTanggalAkhir(Date tanggalAkhir) {
		this.tanggalAkhir = tanggalAkhir;
	}
	
	
	@Column(name="tempat_pkl")
	public String getTempatPKL() {
		return tempatPKL;
	}
	public void setTempatPKL(String tempatPKL) {
		this.tempatPKL = tempatPKL;
	}
	
	@Column
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	
	@Column
	public String getDataAwal() {
		return dataAwal;
	}

	public void setDataAwal(String dataAwal) {
		this.dataAwal = dataAwal;
	}

	@Column
	public String getDataBab1() {
		return dataBab1;
	}

	public void setDataBab1(String dataBab1) {
		this.dataBab1 = dataBab1;
	}

	@Column
	public String getDataBab2() {
		return dataBab2;
	}

	public void setDataBab2(String dataBab2) {
		this.dataBab2 = dataBab2;
	}

	@Column
	public String getDataBab3() {
		return dataBab3;
	}

	public void setDataBab3(String dataBab3) {
		this.dataBab3 = dataBab3;
	}

	@Column
	public String getDataBab4() {
		return dataBab4;
	}

	public void setDataBab4(String dataBab4) {
		this.dataBab4 = dataBab4;
	}

	@Column
	public String getDataBab5() {
		return dataBab5;
	}

	public void setDataBab5(String dataBab5) {
		this.dataBab5 = dataBab5;
	}

	@Column
	public String getDaftarPustaka() {
		return daftarPustaka;
	}

	public void setDaftarPustaka(String daftarPustaka) {
		this.daftarPustaka = daftarPustaka;
	}

	@Column
	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

	@ManyToOne
	@JoinColumn
	public Dosen getDospemPKL() {
		return dospemPKL;
	}

	public void setDospemPKL(Dosen dospemPKL) {
		this.dospemPKL = dospemPKL;
	}

	@Column
	(nullable=true)
	public int getDisiplin() {
		return disiplin;
	}

	public void setDisiplin(int disiplin) {
		this.disiplin = disiplin;
	}

	@Column
	(nullable=true)
	public int getKemampuan() {
		return kemampuan;
	}

	public void setKemampuan(int kemampuan) {
		this.kemampuan = kemampuan;
	}

	@Column
	(nullable=true)
	public int getKerapihan() {
		return kerapihan;
	}

	public void setKerapihan(int kerapihan) {
		this.kerapihan = kerapihan;
	}

	@Column
	(nullable=true)
	public int getNilaiTotal() {
		return nilaiTotal;
	}

	public void setNilaiTotal(int nilaiTotal) {
		this.nilaiTotal = nilaiTotal;
	}

	
}
