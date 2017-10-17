package skripsi.sitpast.act.mhs;

import org.hibernate.Session;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;

public class PengajuanPKLAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8626636095450602074L;
	Textbox judul;
	Textbox tempat;
	
	Session sess;
	Mahasiswa maha;
	PKL pkl;
	Button btn_simpan;
	Button btn_edit;
	int statusInt = 6;
	Label status; //1 diajukan, 2 disetujui/aktiv, 3 lulus, 4.ditolak, 5.belum
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		loadData();
	}

	private void loadData(){
		Mahasiswa mahasiswa;
		
		maha = (Mahasiswa) session.getAttribute("mahasiswa");
		sess = HibernateUtil.getSessionFactory().openSession();
		mahasiswa = (Mahasiswa) sess.createQuery("from Mahasiswa where id="+maha.getId()).uniqueResult();
		System.out.println("maha nama : "+maha.getNama());
		
		if(mahasiswa!=null){
			pkl = mahasiswa.getPkl();
			if(pkl!=null){
				judul.setText(pkl.getJudul());
				judul.setReadonly(true);
				btn_simpan.setVisible(false);
				
				if(pkl.getStatus()==0)
					status.setValue("Sedang Diajukan/Belum disetujui");
				else if(pkl.getStatus()==1)
					status.setValue("Disetujui/Aktiv");
				else if(pkl.getStatus()==2)
					status.setValue("Lulus");
				else if(pkl.getStatus()==3)
					status.setValue("Ditolak");
				
				tempat.setText(pkl.getTempatPKL());
				tempat.setReadonly(true);
			}
				
			maha = mahasiswa;
			sess.disconnect();
			sess.close();
		}
		
	}
	
	
	public void onEdit(){
		judul.setReadonly(false);
		tempat.setReadonly(false);
		btn_edit.setVisible(false);
		btn_simpan.setVisible(true);
	}
	
	public void onSave(){
		Mahasiswa mahasiswa;
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(maha==null)
			mahasiswa = new Mahasiswa();
		else 	
			mahasiswa = maha;
		
		
		PKL pklEdit = new PKL();
		if(pkl!=null)
			pklEdit = pkl;
			
		
		pklEdit.setJudul(judul.getText());
		pklEdit.setTempatPKL(tempat.getValue());
		
		if(pklEdit.getMahasiswa()==null)
			pklEdit.setMahasiswa(mahasiswa);
		
		if(mahasiswa.getPkl()==null)
			mahasiswa.setPkl(pklEdit);
		
		sess.saveOrUpdate(pklEdit);
		sess.getTransaction().commit();
		
		Messagebox.show("Berhasil disimpan", "Simpan data", Messagebox.OK, null, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				loadData();
			}
		});
		

		sess.close();
		
		btn_edit.setVisible(true);
		btn_simpan.setVisible(false);
		
		
	}
}
