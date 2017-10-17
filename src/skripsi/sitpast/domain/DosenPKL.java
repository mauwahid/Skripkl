package skripsi.sitpast.domain;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="dosenpkl")
public class DosenPKL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1441646589270304592L;
	private Long id;
	private Dosen dosen;
	private PKL pkl;
	
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="id_dosen")
	public Dosen getDosen() {
		return dosen;
	}
	public void setDosen(Dosen dosen) {
		this.dosen = dosen;
	}
	
	@ManyToOne
	@JoinColumn(name="id_pkl")
	public PKL getPkl() {
		return pkl;
	}
	public void setPkl(PKL pkl) {
		this.pkl = pkl;
	}
	
	
}
