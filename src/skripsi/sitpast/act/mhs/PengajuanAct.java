package skripsi.sitpast.act.mhs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Skripsi;

public class PengajuanAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1697421139283399643L;
	Textbox judul;
	Session sess;
	Mahasiswa maha;
	Skripsi skripsi;
	Button btn_simpan;
	Button btn_edit;
	int statusInt = 6;
	Label status; //1 diajukan, 2 disetujui/aktiv, 3 lulus, 4.ditolak, 5.belum
	Label lbl_awal;
	Label lbl_bab1;
	
	private String path = "D:\\";
	private String fileName = "";
	
	private final int BTN_AWAL = 0;
	private final int BTN_BAB1 = 2;
	
	

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
			skripsi = mahasiswa.getSkripsi();
			if(skripsi!=null){
				judul.setText(skripsi.getJudulSkripsi());
				judul.setReadonly(true);
				btn_simpan.setVisible(false);
				
				if(skripsi.getStatus()==0)
					status.setValue("Sedang Diajukan/Belum disetujui");
				else if(skripsi.getStatus()==1)
					status.setValue("Disetujui/Aktiv");
				else if(skripsi.getStatus()==2)
					status.setValue("Lulus");
				else if(skripsi.getStatus()==3)
					status.setValue("Ditolak");
				
				lbl_awal.setValue(skripsi.getDataAwal()==null?"Kosong":skripsi.getDataAwal());
				lbl_bab1.setValue(skripsi.getDataBab1()==null?"Kosong":skripsi.getDataBab1());
				
					
			}
				
			maha = mahasiswa;
			sess.disconnect();
			sess.close();
		}
		
	}
	
	
	public void onEdit(){
		judul.setReadonly(false);
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
		
		Skripsi skripsiEdit = new Skripsi();
		if(skripsi!=null)
			skripsiEdit = skripsi;
			
		
		skripsiEdit.setJudulSkripsi(judul.getText());
	
		if(skripsi!=null)
		if(skripsi.getMahasiswa()==null)
			skripsiEdit.setMahasiswa(mahasiswa);
		
		if(skripsiEdit.getMahasiswa()==null)
			skripsiEdit.setMahasiswa(mahasiswa);
			
		sess.saveOrUpdate(skripsiEdit);
		sess.getTransaction().commit();
		
		
		
		
		Messagebox.show("Berhasil disimpan", "Simpan data", Messagebox.OK, null, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, 7);
				Date date = cal.getTime();
				
				Messagebox.show("Info diterima/ditolak akan terlihat max "+date.toString());
				loadData();
			}
		});
		

		sess.close();
		
		btn_edit.setVisible(true);
		btn_simpan.setVisible(false);
		
		
		
	}
	
	
	
	private void loadDatabase(){
		Mahasiswa mahasiswa;
		sess = HibernateUtil.getSessionFactory().openSession();
		System.out.println("NIM : "+maha.getId());
		mahasiswa = (Mahasiswa) sess.createQuery("from Mahasiswa where id="+maha.getId()).uniqueResult();
		
		if(mahasiswa!=null){
			skripsi = mahasiswa.getSkripsi();
		}
		sess.beginTransaction();
	}
		
		public void onSaveData(int idButton,String value){
			loadDatabase();
			
			switch(idButton){
			case BTN_AWAL :
				skripsi.setDataAwal(value);
				sess.update(skripsi);
				sess.getTransaction().commit();
				sess.close();
			break;
			case BTN_BAB1 :
				skripsi.setDataBab1(value);
				sess.update(skripsi);
				sess.getTransaction().commit();
				sess.close();
			break;
			}
			
		}
		
		public void onUplAwal(UploadEvent event){
			Media media = event.getMedia();
			final String mediaName = fileName+"_"+media.getName();
			
			if(media!=null){
				
				String name= media.getName();
				
				File file = new File(path+"\\"+mediaName);
				try {
					Files.copy(file, media.getStreamData());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("gagal");
				}
				
				Messagebox.show("Simpan data "+media.getName(), "Sukses", Messagebox.OK, null, new EventListener<Event>() {
					
					
					
					@Override
					public void onEvent(Event event) throws Exception {
						// TODO AutoloadData();
						onSaveData(BTN_AWAL, mediaName);
					}
				});
				
				lbl_awal.setValue(mediaName);
				
			}
			else{
				Messagebox.show("Gagal", "Gagal", Messagebox.OK, null, new EventListener<Event>() {
					
					@Override
					public void onEvent(Event event) throws Exception {
						// TODO AutoloadData();
					}
				});
			}
			
		}
		
		public void onUplBab1(UploadEvent event){
			Media media = event.getMedia();
			final String mediaName = fileName+"_"+media.getName();
			
			if(media!=null){
				
				String name= media.getName();
				
				File file = new File(path+"\\"+mediaName);
				try {
					Files.copy(file, media.getStreamData());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("gagal");
				}
				
				Messagebox.show("Simpan data "+media.getName(), "Sukses", Messagebox.OK, null, new EventListener<Event>() {
					
					
					
					@Override
					public void onEvent(Event event) throws Exception {
						// TODO AutoloadData();
						onSaveData(BTN_BAB1, mediaName);
					}
				});
				
				lbl_bab1.setValue(mediaName);
				
			}
			else{
				Messagebox.show("Gagal", "Gagal", Messagebox.OK, null, new EventListener<Event>() {
					
					@Override
					public void onEvent(Event event) throws Exception {
						// TODO AutoloadData();
					}
				});
			}
			
		}
		
		
		public void onDownloadAwal(){
			try {
				download(BTN_AWAL);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void onDownloadBab1(){
			try {
				download(BTN_AWAL);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void download(int idButton) throws FileNotFoundException{
			loadDatabase();
			File file;
			switch(idButton){
			case BTN_AWAL :
				file = new File(path+"\\"+skripsi.getDataAwal());
				Filedownload.save(file,null);
				System.out.println("Sukses");
			break;
			case BTN_BAB1:
				file = new File(path+"\\"+skripsi.getDataBab1());
				Filedownload.save(file,null);
				System.out.println("Sukses");
			break;
			}
		}
}
