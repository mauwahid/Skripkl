package skripsi.sitpast.domain;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dosen")
public class Pudek implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8479030159176490532L;

	private Long id;
	private String nip;
	private String nama;
	private String Alamat;
	private String noTelp;
	

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
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
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
	
	
	
	
}
