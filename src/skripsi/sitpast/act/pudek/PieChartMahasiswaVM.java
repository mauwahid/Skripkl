package skripsi.sitpast.act.pudek;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.PieModel;

import skripsi.sitpast.domain.Prodi;
import skripsi.sitpast.domain.Skripsi;

public class PieChartMahasiswaVM {

	PieChartMahasiswaEngine engine;
	PieModel model;
	boolean threeD = true;
	String message;
	Session sess;
	List<Prodi> listProdi;
	List<Skripsi> listMahasiswa;
	
	@Init
	public void init() {
		// prepare chart data
		engine = new PieChartMahasiswaEngine();

		model = ChartData.getModel();
	}

	public PieChartMahasiswaEngine getEngine() {
		return engine;
	}

	public PieModel getModel() {
		return model;
	}

	public boolean isThreeD() {
		return threeD;
	}
	
	public String getMessage(){
		return message;
	}
	
	@Command("showMessage") 
	@NotifyChange("message")
	public void onShowMessage(
			@BindingParam("msg") String message){
		this.message = message;
	}
	
	@GlobalCommand("dataChanged") 
	@NotifyChange("model")
	public void onDataChanged(
			@BindingParam("tahun")String tahun){
		
		sess = HibernateUtil.getSessionFactory().openSession();
		
		
		listProdi = sess.createCriteria(Prodi.class).list();
		
		
		Prodi prodi;
		String jumlah;
		
		Iterator<Prodi> iterProdi = listProdi.iterator();
		
		while(iterProdi.hasNext()){
			prodi = iterProdi.next();
			//jumlah = (String) sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = "+prodi.getId()+" and tanggalsidang like '%"+tahun+"%'").uniqueResult();
		//	model.setValue(prodi.getNamaProdi(),new Double(Integer.parseInt(jumlah)));
			listMahasiswa= sess.createQuery("select distinct mahasiswa from Mahasiswa as mahasiswa " +
				"where mahasiswa.angkatan = '"+tahun+"'").list();
			model.setValue(prodi.getNamaProdi(), listMahasiswa.size());
		}
		
			
	//	System.out.println("halooo.."+tahun);
	
	//	String sJml = (String) criteria.uniqueResult();
//		int jumlah = Integer.parseInt(sJml);
		
		/*model.setValue("Sistem Informasi", 10);
		model.setValue("Matematika", 20);
		model.setValue("Agribisnis", 10);
		model.setValue("Teknik Informatika", 15);*/
//		model.setValue("Fisika", new Double(jumlah.intValue()));
//		model.setValue("Kimia", new Double(jumlah.intValue()));
//		model.setValue("Biologi", new Double(jumlah.intValue()));
//		model.setValue("Teknik Informatika", new Double(jumlah.intValue()));
//		
	}
	
	
	
	@GlobalCommand("configChanged") 
	@NotifyChange({"threeD","engine"})
	public void onConfigChanged(
			@BindingParam("threeD") boolean threeD,
			@BindingParam("exploded") boolean exploded){
		this.threeD = threeD;
		engine.setExplode(exploded);
	}
}
