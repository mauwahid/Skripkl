package skripsi.sitpast.act.dosen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;


import skripsi.sitpast.act.dosen.BimbinganSkripsiAct.BimbinganSkripsiRenderer;
import skripsi.sitpast.domain.BimbinganSkripsi;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Skripsi;
import skripsi.sitpast.domain.Skripsi;

public class PenilaianSkripsiAct extends GenericForwardComposer<Component>{

	Dosen dosen;
	Combobox combo_mahasiswa;
	Session sess;
	List<Mahasiswa> listMahasiswa;
	Textbox nilai_penampilan;
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
		
		
		Iterator<Skripsi> iter = dosen.getDospemSatu().iterator();
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

	public void onLoadSkripsi(){
		maha = (Mahasiswa) combo_mahasiswa.getSelectedItem().getValue();
		showDataSkripsi(maha);
	}
	
	
	private void showDataSkripsi(Mahasiswa data){
		
		
		sess = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = sess.createCriteria(Skripsi.class);
		Skripsi skripsi = (Skripsi) criteria.add(Restrictions.eq("id", data.getId())).uniqueResult();
		
		nilai_penampilan.setText(skripsi.getPenampilan()+"");
		nilai_kemampuan.setText(skripsi.getKemampuan()+"");
		nilai_kerapihan.setText(skripsi.getKerapihan()+"");
		nilai_total.setValue(skripsi.getNilaiTotal()+"");
		
		sess.close();
		
	}
	
	
	public void onEdit(){
		nilai_penampilan.setReadonly(false);
		nilai_kemampuan.setReadonly(false);
		nilai_kerapihan.setReadonly(false);
		btn_edit.setVisible(false);
		btn_simpan.setVisible(true);

	}
	
	public void onSave(){
		Mahasiswa mahasiswa;
		sess = HibernateUtil.getSessionFactory().openSession();
		
		sess.beginTransaction();
		Skripsi skripsi = (Skripsi) sess.createCriteria(Skripsi.class).add(Restrictions.eq("id", maha.getId())).uniqueResult();
		
		int mampu = Integer.parseInt(nilai_kemampuan.getValue());
		int penampilan = Integer.parseInt(nilai_penampilan.getValue());
		int rapih = Integer.parseInt(nilai_kerapihan.getValue());
		
		skripsi.setKemampuan(mampu);
		skripsi.setKerapihan(rapih);
		skripsi.setPenampilan(penampilan);
		
		int total = (mampu+penampilan+rapih)/3;
		
		skripsi.setNilaiTotal(total);
				
		sess.saveOrUpdate(skripsi);
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
