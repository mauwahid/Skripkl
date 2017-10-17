package skripsi.sitpast.act.mhs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zk.ui.Component;
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


import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Pesan;


public class FormPesanAct extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1069494637390923433L;
	Textbox judul;
	Textbox isi;
	ListModel model;
	List<Dosen> listDosen;
	
	Dosen toDosen;
	Mahasiswa fromMhs;

	Session sess;
	Pesan pesan;
	
	
	Combobox combo_to_dosen;
	ListModel<Dosen> modelDosen;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		
		fromMhs = (Mahasiswa) session.getAttribute("mahasiswa");
		if(fromMhs.getSkripsi()!=null)
			loadDosen();
	}
	
	private void loadDosen(){
		combo_to_dosen.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listDosen = new ArrayList<Dosen>();
		listDosen.add(fromMhs.getSkripsi().getDosPemSatu());
		listDosen.add(fromMhs.getSkripsi().getDosPemDua());
		modelDosen = new SimpleListModel<Dosen>(listDosen);
		
		ListModelList<Dosen> listModel = new ListModelList<Dosen>(listDosen);
		listModel.addSelection(listModel.get(0));
		
		combo_to_dosen.setModel(listModel);
		combo_to_dosen.setItemRenderer(new DosenRenderer());
		combo_to_dosen.getItemRenderer();
	
		
		
		System.out.println("render selesai");
	}
	
	class DosenRenderer implements ComboitemRenderer<Dosen>{

		@Override
		public void render(Comboitem item, Dosen data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			item.setValue(data);
			item.setLabel(data.getNama());
			System.out.println("data : "+data.getNama());
			
		}
		
	}
	
	
	public void onSend() throws InterruptedException{
			sendToDosen();
		
	}
	

	
	private void sendToDosen(){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		pesan = new Pesan();
		toDosen = (Dosen) combo_to_dosen.getSelectedItem().getValue();
		pesan.setToDosen(toDosen);
		pesan.setFromMhs(fromMhs);
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
