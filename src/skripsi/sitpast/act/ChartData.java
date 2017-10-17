package skripsi.sitpast.act;

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
		
		
		
		BigInteger jumlah = (BigInteger) query.uniqueResult();
		
	
	//	String sJml = (String) criteria.uniqueResult();
//		int jumlah = Integer.parseInt(sJml);
		
		model.setValue("C/C++", new Double(21.2));
		model.setValue("VB", new Double(10.2));
		model.setValue("Java", new Double(40.4));
		model.setValue("PHP", new Double(jumlah.intValue()));
		return model;
	}
	
	
	public void valueTI(){
		
	}
}
