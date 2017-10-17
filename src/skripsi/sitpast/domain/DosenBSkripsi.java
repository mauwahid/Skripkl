package skripsi.sitpast.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class DosenBSkripsi {

	private Long id;
	private Dosen dosen;
	private BimbinganSkripsi bimbingan;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="dosen_id")
	public Dosen getDosen() {
		return dosen;
	}
	public void setDosen(Dosen dosen) {
		this.dosen = dosen;
	}
	
	@ManyToOne
	@JoinColumn(name="bimbingan_id")
	public BimbinganSkripsi getBimbingan() {
		return bimbingan;
	}
	public void setBimbingan(BimbinganSkripsi bimbingan) {
		this.bimbingan = bimbingan;
	}
	
}
