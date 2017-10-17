package skripsi.sitpast.act.dosen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;


import skripsi.sitpast.act.dosen.BimbinganPKLAct.BimbinganPKLRenderer;
import skripsi.sitpast.domain.BimbinganPKL;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;
import skripsi.sitpast.domain.Skripsi;

public class PenilaianPKLAct extends GenericForwardComposer<Component>{

	Dosen dosen;
	Combobox combo_mahasiswa;
	Session sess;
	List<Mahasiswa> listMahasiswa;
	Textbox nilai_kedisiplinan;
	Textbox nilai_kerapihan;
	Textbox nilai_kemampuan;
	Label nilai_total;
	Mahasiswa maha;
	Button btn_edit;
	Button btn_simpan;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		dosen = (Dosen) session.getAttribute("dosen");
		loadComboMhs();
	}
	
	private void loadComboMhs(){
		
		combo_mahasiswa.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listMahasiswa = new ArrayList<Mahasiswa>();
		Mahasiswa mhs;
		
		
		Iterator<PKL> iter = dosen.getDospemPKL().iterator();
		while(iter.hasNext()){
			mhs = iter.next().getMahasiswa();
			System.out.println("Nama MHs : "+mhs.getNama());
			listMahasiswa.add(mhs);
		}
		
	//	modelMahasiswa = new SimpleListModel<Mahasiswa>(listMahasiswa);
		ListModelList<Mahasiswa> listModel = new ListModelList<Mahasiswa>(listMahasiswa);
		
		combo_mahasiswa.setModel(listModel);
		combo_mahasiswa.setItemRenderer(new MhsRenderer());
		combo_mahasiswa.getItemRenderer();
		
		
		System.out.println("render selesai");
	}
	
	private class MhsRenderer implements ComboitemRenderer<Mahasiswa>{

		@Override
		public void render(Comboitem item, Mahasiswa data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			item.setValue(data);
			item.setLabel(data.getNama());
			System.out.println("data : "+data.getNama());
			
		}
		
	}

	public void onLoadPKL(){
		maha = (Mahasiswa) combo_mahasiswa.getSelectedItem().getValue();
		showDataPKL(maha);
	}
	
	
	private void showDataPKL(Mahasiswa data){
		
		
		sess = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = sess.createCriteria(PKL.class);
		PKL pkl = (PKL) criteria.add(Restrictions.eq("id", data.getId())).uniqueResult();
		
		nilai_kedisiplinan.setText(pkl.getDisiplin()+"");
		nilai_kemampuan.setText(pkl.getKemampuan()+"");
		nilai_kerapihan.setText(pkl.getKerapihan()+"");
		nilai_total.setValue(pkl.getNilaiTotal()+"");
		
		sess.close();
		
	}
	
	
	public void onEdit(){
		nilai_kedisiplinan.setReadonly(false);
		nilai_kemampuan.setReadonly(false);
		nilai_kerapihan.setReadonly(false);
		btn_edit.setVisible(false);
		btn_simpan.setVisible(true);

	}
	
	
	
	public void onPrint() throws JRException, FileNotFoundException{
		
		Session sess = HibernateUtil.getSessionFactory().openSession();
		PKL pkl = (PKL) sess.createCriteria(PKL.class).add(Restrictions.eq("id", maha.getId())).uniqueResult();
		
		Map params = new HashMap();
	
			Jasperreport report = new Jasperreport();
           // report.setParent(box);
            report.setHeight("500px");
            Map parameters = new HashMap();
            
            parameters.put("nim",pkl.getMahasiswa().getNim()+"");
            parameters.put("namaMhs",pkl.getMahasiswa().getNama());
            parameters.put("disiplin",pkl.getDisiplin()+"");
            parameters.put("mampu", pkl.getKemampuan()+"");
            parameters.put("rapih", pkl.getKerapihan()+"");
            parameters.put("total",pkl.getNilaiTotal()+"");
            parameters.put("jurusan", pkl.getMahasiswa().getProdi().getNamaProdi());
    	//	parameters.put("id_mhs",aju_bea.getMahasiswa().getId());
    		
            Connection conn = HibernateUtil.getSessionFactory().openSession().connection();
            if(conn==null)
            	System.out.println("conn null");
            JasperPrint jasperPrint = null;
            if(parameters==null)
            	System.out.println("paramters null");
            
           
				jasperPrint = JasperFillManager.fillReport(application.getRealPath("/backend/data/laporanNilai.jasper"),
				        parameters, new JREmptyDataSource());
		
            File myFile = new File(application
                    .getRealPath("/backend/data/laporanNilai.pdf"));
            myFile.getParentFile().mkdirs();
            JRAbstractExporter exporterPDF = new JRPdfExporter();
            exporterPDF.setParameter(JRXlsExporterParameter.JASPER_PRINT,
                    jasperPrint);
            
				exporterPDF.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
				        new FileOutputStream(myFile));
			
            exporterPDF.setParameter(JRExporterParameter.OUTPUT_FILE,
                    myFile);
            try {
				exporterPDF.exportReport();
			} catch (JRException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				Filedownload.save(myFile, "pdf");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             
	}
	
	
	public void onSave(){
		Mahasiswa mahasiswa;
		sess = HibernateUtil.getSessionFactory().openSession();
		
		sess.beginTransaction();
		PKL pkl = (PKL) sess.createCriteria(PKL.class).add(Restrictions.eq("id", maha.getId())).uniqueResult();
		
		int mampu = Integer.parseInt(nilai_kemampuan.getValue());
		int disiplin = Integer.parseInt(nilai_kedisiplinan.getValue());
		int rapih = Integer.parseInt(nilai_kerapihan.getValue());
		
		pkl.setKemampuan(mampu);
		pkl.setKerapihan(rapih);
		pkl.setDisiplin(disiplin);
		
		int total = (mampu+disiplin+rapih)/3;
		
		pkl.setNilaiTotal(total);
				
		sess.saveOrUpdate(pkl);
		sess.getTransaction().commit();
		
		Messagebox.show("Berhasil disimpan", "Simpan data", Messagebox.OK, null, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				loadComboMhs();
			}
		});
		

		sess.close();
		
		btn_edit.setVisible(true);
		btn_simpan.setVisible(false);
		
	}
	
}
