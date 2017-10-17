package skripsi.sitpast.act.dosen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import antlr.debug.Event;


import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Pesan;
import skripsi.sitpast.domain.Prodi;
import skripsi.sitpast.domain.Skripsi;
import skripsi.sitpast.domain.UserTable;


public class FormPesanAct extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1069494637390923433L;
	Textbox judul;
	Textbox isi;
	ListModel model;
	List<Mahasiswa> listMahasiswa;
	
	List<UserTable> listProdi;
	
	Dosen fromDosen;
	Mahasiswa toMhs;
	UserTable toProdi;

	Session sess;
	Pesan pesan;
	
	
	Combobox combo_to_mhs;
	Combobox combo_option;
	Combobox combo_to_prodi;
	ListModel<Mahasiswa> modelMahasiswa;
	ListModel<UserTable> modelProdi;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		
		fromDosen = (Dosen) session.getAttribute("dosen");
		if(fromDosen.getDospemSatu()!=null){
			loadMahasiswa();
			loadProdi();
		}
	}
	
	
	private void loadProdi(){
		combo_to_prodi.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listProdi = sess.createCriteria(UserTable.class).add(Restrictions.eq("prodi",fromDosen.getProdi())).list();
		
		UserTable prodi;
		modelProdi = new SimpleListModel<UserTable>(listProdi);
		
		ListModelList<UserTable> listModel = new ListModelList<UserTable>(listProdi);
		
		combo_to_mhs.setModel(listModel);
		combo_to_mhs.setItemRenderer(new ProdiRenderer());
		combo_to_mhs.getItemRenderer();
	
		
		
		System.out.println("render selesai");
	}
	
	
	public void onComboChange(){
		
		System.out.println("Cek change");
		if(combo_option.getSelectedIndex()==0){
			combo_to_mhs.setVisible(true);
			combo_to_prodi.setVisible(false);
		}else{
			combo_to_mhs.setVisible(false);
			combo_to_prodi.setVisible(true);
		}
	}
	
	private void loadMahasiswa(){
		combo_to_mhs.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listMahasiswa = new ArrayList<Mahasiswa>();
		
		Mahasiswa mhs;
		
		Iterator<Skripsi> iter = fromDosen.getDospemSatu().iterator();
		while(iter.hasNext()){
			mhs = iter.next().getMahasiswa();
			System.out.println("Nama MHs : "+mhs.getNama());
			listMahasiswa.add(mhs);
		}
		
		Iterator<Skripsi> iterDosen = fromDosen.getDospemDua().iterator();
		while(iterDosen.hasNext()){
			mhs = iterDosen.next().getMahasiswa();
			System.out.println("nama mahasiswa 2 : "+mhs.getNama());
			listMahasiswa.add(mhs);
		}
		
		
		
		modelMahasiswa = new SimpleListModel<Mahasiswa>(listMahasiswa);
		
		ListModelList<Mahasiswa> listModel = new ListModelList<Mahasiswa>(listMahasiswa);
		listModel.addSelection(listModel.get(0));
		
		combo_to_mhs.setModel(listModel);
		combo_to_mhs.setItemRenderer(new MahasiswaRenderer());
		combo_to_mhs.getItemRenderer();
	
		
		
		System.out.println("render selesai");
	}
	
	
	private void loadDosen(){
		
	}
	
	class MahasiswaRenderer implements ComboitemRenderer<Mahasiswa>{

		@Override
		public void render(Comboitem item, Mahasiswa data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			item.setValue(data);
			item.setLabel(data.getNama());
			System.out.println("data : "+data.getNama());
			
		}
		
	}
	
	class ProdiRenderer implements ComboitemRenderer<UserTable>{

		@Override
		public void render(Comboitem item, UserTable data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			item.setValue(data);
			item.setLabel(data.getNamaLengkap());
			System.out.println("data : "+data.getNamaLengkap());
			
		}
		
	}
	
	
	public void onSend() throws InterruptedException{
		if(combo_option.getSelectedIndex()==0)	
			sendToMhs();
		else
			sendToProdi();
	}
	

	
	private void sendToMhs(){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		pesan = new Pesan();
		toMhs = (Mahasiswa) combo_to_mhs.getSelectedItem().getValue();
		pesan.setToMhs(toMhs);
		pesan.setFromDosen(fromDosen);
		pesan.setJudul(judul.getValue());
		pesan.setIsi(isi.getValue());
		pesan.setWaktu(Calendar.getInstance().getTime());
		sess.save(pesan);
		sess.getTransaction().commit();
		Messagebox.show("Pesan telah dikirim");
		
		isi.setValue("");
		judul.setValue("");
	}
	
	private void sendToProdi(){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		pesan = new Pesan();
		toMhs = (Mahasiswa) combo_to_mhs.getSelectedItem().getValue();
		pesan.setToMhs(toMhs);
		pesan.setFromDosen(fromDosen);
		pesan.setJudul(judul.getValue());
		pesan.setIsi(isi.getValue());
		pesan.setWaktu(Calendar.getInstance().getTime());
		sess.save(pesan);
		sess.getTransaction().commit();
		Messagebox.show("Pesan telah dikirim");
		
		isi.setValue("");
		judul.setValue("");
	}
	

}
