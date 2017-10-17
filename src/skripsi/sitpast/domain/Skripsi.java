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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="skripsi")
public class Skripsi implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2395840485772203543L;

	private Long Id;
	private Mahasiswa mahasiswa;
	private String judulSkripsi;
	private Date tanggalMulai;
	private Date tanggalSelesai;
	private Date tanggalSeminar;
	private Date tanggalSidang;
	private Date tanggalSeminarProp;
	private Dosen dosPemSatu;
	private Dosen dosPemDua;
	private Dosen dosUjiSatu;
	private Dosen dosUjiDua;
	private String dataPengesahan;
	private String dataAwal;
	private String dataBab1;
	private String dataBab2;
	private String dataBab3;
	private String dataBab4;
	private String dataBab5;
	private String daftarPustaka;
	private String lampiran;
	private int penampilan;
	private int kemampuan;
	private int kerapihan;
	private int nilaiTotal;
	private int validasi;// 0 : belum divalidasi, 1 : validasi dospem 1, 2 : validasi dospem 2, 3 : validasi dospem 1 dan 2
	
	private int status;
	private List<BimbinganSkripsi> bimbinganSkripsi = new ArrayList<BimbinganSkripsi>();

	 @Id
	 @Column(name="id", unique=true, nullable=false)
	 @GeneratedValue(generator="gen")
	 @GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(name="property", value="mahasiswa"))
	public Long getId() {
		return Id;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	public void setId(Long id) {
		this.Id = id;
	}
	
	
	@Column
	public String getJudulSkripsi() {
		return judulSkripsi;
	}
	public void setJudulSkripsi(String judulSkripsi) {
		this.judulSkripsi = judulSkripsi;
	}
	

	

	@OneToMany(mappedBy="skripsi",targetEntity=BimbinganSkripsi.class,fetch=FetchType.LAZY)
	public List<BimbinganSkripsi> getBimbinganSkripsi() {
		return bimbinganSkripsi;
	}

	public void setBimbinganSkripsi(List<BimbinganSkripsi> bimbinganSkripsi) {
		this.bimbinganSkripsi = bimbinganSkripsi;
	}

	@Column
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	@Column
	public Date getTanggalMulai() {
		return tanggalMulai;
	}

	public void setTanggalMulai(Date tanggalMulai) {
		this.tanggalMulai = tanggalMulai;
	}

	@Column
	public Date getTanggalSelesai() {
		return tanggalSelesai;
	}

	public void setTanggalSelesai(Date tanggalSelesai) {
		this.tanggalSelesai = tanggalSelesai;
	}

	@Column
	public Date getTanggalSeminar() {
		return tanggalSeminar;
	}

	public void setTanggalSeminar(Date tanggalSeminar) {
		this.tanggalSeminar = tanggalSeminar;
	}

	@Column
	public Date getTanggalSidang() {
		return tanggalSidang;
	}

	public void setTanggalSidang(Date tanggalSidang) {
		this.tanggalSidang = tanggalSidang;
	}

	@ManyToOne
	@JoinColumn(name="dospem_satu")
	public Dosen getDosPemSatu() {
		return dosPemSatu;
	}

	public void setDosPemSatu(Dosen dosPemSatu) {
		this.dosPemSatu = dosPemSatu;
	}

	@ManyToOne
	@JoinColumn(name="dospem_dua")
	public Dosen getDosPemDua() {
		return dosPemDua;
	}

	public void setDosPemDua(Dosen dosPemDua) {
		this.dosPemDua = dosPemDua;
	}

	@ManyToOne
	@JoinColumn(name="dosuji_satu")
	public Dosen getDosUjiSatu() {
		return dosUjiSatu;
	}

	public void setDosUjiSatu(Dosen dosUjiSatu) {
		this.dosUjiSatu = dosUjiSatu;
	}

	@ManyToOne
	@JoinColumn(name="dosuji_dua")
	public Dosen getDosUjiDua() {
		return dosUjiDua;
	}

	public void setDosUjiDua(Dosen dosUjiDua) {
		this.dosUjiDua = dosUjiDua;
	}

	public String getDataPengesahan() {
		return dataPengesahan;
	}

	public void setDataPengesahan(String dataPengesahan) {
		this.dataPengesahan = dataPengesahan;
	}

	public String getDataAwal() {
		return dataAwal;
	}

	public void setDataAwal(String dataAwal) {
		this.dataAwal = dataAwal;
	}

	public String getDataBab1() {
		return dataBab1;
	}

	public void setDataBab1(String dataBab1) {
		this.dataBab1 = dataBab1;
	}

	public String getDataBab2() {
		return dataBab2;
	}

	public void setDataBab2(String dataBab2) {
		this.dataBab2 = dataBab2;
	}

	public String getDataBab3() {
		return dataBab3;
	}

	public void setDataBab3(String dataBab3) {
		this.dataBab3 = dataBab3;
	}

	public String getDataBab4() {
		return dataBab4;
	}

	public void setDataBab4(String dataBab4) {
		this.dataBab4 = dataBab4;
	}

	public String getDataBab5() {
		return dataBab5;
	}

	public void setDataBab5(String dataBab5) {
		this.dataBab5 = dataBab5;
	}

	public String getDaftarPustaka() {
		return daftarPustaka;
	}

	public void setDaftarPustaka(String daftarPustaka) {
		this.daftarPustaka = daftarPustaka;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

	@Column
	public Date getTanggalSeminarProp() {
		return tanggalSeminarProp;
	}

	public void setTanggalSeminarProp(Date tanggalSeminarProp) {
		this.tanggalSeminarProp = tanggalSeminarProp;
	}

	@Column(nullable=true)
	public int getValidasi() {
		return validasi;
	}

	public void setValidasi(int validasi) {
		this.validasi = validasi;
	}

	
	@Column(nullable=true)
	public int getKemampuan() {
		return kemampuan;
	}

	public void setKemampuan(int kemampuan) {
		this.kemampuan = kemampuan;
	}

	@Column(nullable=true)
	public int getKerapihan() {
		return kerapihan;
	}

	public void setKerapihan(int kerapihan) {
		this.kerapihan = kerapihan;
	}

	@Column(nullable=true)
	public int getNilaiTotal() {
		return nilaiTotal;
	}

	public void setNilaiTotal(int nilaiTotal) {
		this.nilaiTotal = nilaiTotal;
	}

	@Column(nullable=true)
	public int getPenampilan() {
		return penampilan;
	}

	public void setPenampilan(int penampilan) {
		this.penampilan = penampilan;
	}
	
	
	
}
