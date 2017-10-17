package skripsi.sitpast.act.prodi;

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

public class PieChartSkripsiVM {

	PieChartSkripsiEngine engine;
	PieModel model;
	boolean threeD = true;
	String message;
	Session sess;
	List<Prodi> listProdi;
	List<Skripsi> listSkripsi;
	
	@Init
	public void init() {
		// prepare chart data
		engine = new PieChartSkripsiEngine();

		model = ChartData.getModel();
	}

	public PieChartSkripsiEngine getEngine() {
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
		
//		Query query = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 1 and tanggalsidang like '%"+tahun+"%'");
//		Query query2 = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 2 and tanggalsidang like '%"+tahun+"%'");
//		Query query3 = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 3 and tanggalsidang like '%"+tahun+"%'");
//		Query query4 = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 4 and tanggalsidang like '%"+tahun+"%'	");
//		
//		
//		BigInteger jumlah = (BigInteger) query.uniqueResult();
//		BigInteger jumlah2 = (BigInteger) query2.uniqueResult();
//		BigInteger jumlah3 = (BigInteger) query3.uniqueResult();
//		BigInteger jumlah4 = (BigInteger) query4.uniqueResult();
//		
		
		
		listProdi = sess.createCriteria(Prodi.class).list();
		
		
		Prodi prodi;
		String jumlah;
		
		Iterator<Prodi> iterProdi = listProdi.iterator();
		
		while(iterProdi.hasNext()){
			prodi = iterProdi.next();
			//jumlah = (String) sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = "+prodi.getId()+" and tanggalsidang like '%"+tahun+"%'").uniqueResult();
		//	model.setValue(prodi.getNamaProdi(),new Double(Integer.parseInt(jumlah)));
			listSkripsi = sess.createQuery("select distinct skripsi from Skripsi as skripsi, Mahasiswa as mahasiswa join skripsi.mahasiswa " +
				"where mahasiswa.prodi.id = "+prodi.getId()+" and skripsi.tanggalSidang >= '"+tahun+"-01-01' and skripsi.tanggalSidang <= '"+tahun+"-12-31'").list();
			model.setValue(prodi.getNamaProdi(), listSkripsi.size());
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
