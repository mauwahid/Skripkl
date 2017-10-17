package skripsi.sitpast.act.prodi;

import java.math.BigInteger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimplePieModel;

import skripsi.sitpast.domain.Skripsi;

public class ChartData {

	Session sess;
	int jumlah = 14;
	
	public static PieModel getModel(){
		PieModel model = new SimplePieModel();
		
		Session sess = HibernateUtil.getSessionFactory().openSession();
		sess = HibernateUtil.getSessionFactory().openSession();
		Query query = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 1");
		Query query2 = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 2");
		Query query3 = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 3");
		Query query4 = sess.createSQLQuery("Select count(*) from skripsi, mahasiswa, prodi where skripsi.status = 1 and mahasiswa.id_prodi = 4");
		
		
		BigInteger jumlah = (BigInteger) query.uniqueResult();
		
	
	//	String sJml = (String) criteria.uniqueResult();
//		int jumlah = Integer.parseInt(sJml);
		
		model.setValue("Sistem Informasi", new Double(21.2));
		model.setValue("Matematika", new Double(10.2));
		model.setValue("Agribisnis", new Double(40.4));
		model.setValue("Teknik Informatika", new Double(jumlah.intValue()));
		model.setValue("Fisika", new Double(jumlah.intValue()));
		model.setValue("Kimia", new Double(jumlah.intValue()));
		model.setValue("Biologi", new Double(jumlah.intValue()));
		model.setValue("Teknik Informatika", new Double(jumlah.intValue()));
		
		return model;
	}
	
	
	public void valueTI(){
		
	}
}
