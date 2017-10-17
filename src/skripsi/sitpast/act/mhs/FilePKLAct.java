package skripsi.sitpast.act.mhs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.hibernate.Session;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;

public class FilePKLAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 692246191745149822L;
	private String path = "D:\\";
	private String fileName = "";
	
	private final int BTN_AWAL = 0;
	private final int BTN_BAB1 = 1;
	private final int BTN_BAB2 = 2;
	private final int BTN_BAB3 = 3;
	private final int BTN_BAB4 = 4;
	private final int BTN_BAB5 = 5;
	private final int BTN_DAPUS = 6;
	private final int BTN_LAMP = 7;
	
	
	Mahasiswa mahasiswa;
	Mahasiswa maha;
	Session sess;
	PKL pkl;
	
	Label lbl_awal;
	Label lbl_bab1;
	Label lbl_bab2;
	Label lbl_bab3;
	Label lbl_bab4;
	Label lbl_bab5;
	Label lbl_dapus;
	Label lbl_lampiran;
	
	
	
	String uplAwal;
	String uplBab1;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		maha = (Mahasiswa)session.getAttribute("mahasiswa");
		fileName = maha.getNama()+"_"+maha.getNim()+"_Pkl_";
		
		if(maha.getPkl()!=null)
			pkl = maha.getPkl();
		
		loadDataPkl();
	}
	
	
	private void loadDataPkl(){
		loadDatabase();
		
		lbl_awal.setValue(pkl.getDataAwal()==null?"Kosong":pkl.getDataAwal());
		lbl_bab1.setValue(pkl.getDataBab1()==null?"Kosong":pkl.getDataBab1());
		lbl_bab2.setValue(pkl.getDataBab2()==null?"Kosong":pkl.getDataBab2());
		lbl_bab3.setValue(pkl.getDataBab3()==null?"Kosong":pkl.getDataBab3());
		lbl_bab4.setValue(pkl.getDataBab4()==null?"Kosong":pkl.getDataBab4());
		lbl_bab5.setValue(pkl.getDataBab5()==null?"Kosong":pkl.getDataBab5());
		lbl_lampiran.setValue(pkl.getLampiran()==null?"Kosong":pkl.getLampiran());
		lbl_dapus.setValue(pkl.getDaftarPustaka()==null?"Kosong":pkl.getDaftarPustaka());
		sess.close();
	}

	
	private void loadDatabase(){
		
		sess = HibernateUtil.getSessionFactory().openSession();
		System.out.println("NIM : "+maha.getId());
		mahasiswa = (Mahasiswa) sess.createQuery("from Mahasiswa where id="+maha.getId()).uniqueResult();
		
		if(mahasiswa!=null){
			pkl = mahasiswa.getPkl();
		}
		sess.beginTransaction();
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
					onSave(BTN_AWAL, mediaName);
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
					onSave(BTN_BAB1, mediaName);
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
	
	public void onUplBab2(UploadEvent event){
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
					onSave(BTN_BAB2, mediaName);
				}
			});
			
			lbl_bab2.setValue(mediaName);
			
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
	
	public void onUplBab3(UploadEvent event){
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
					onSave(BTN_BAB3, mediaName);
				}
			});
			
			lbl_bab3.setValue(mediaName);
			
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
	
	public void onUplBab4(UploadEvent event){
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
					onSave(BTN_BAB4, mediaName);
				}
			});
			
			lbl_bab4.setValue(mediaName);
			
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
	
	public void onUplBab5(UploadEvent event){
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
					onSave(BTN_BAB5, mediaName);
				}
			});
			
			lbl_bab5.setValue(mediaName);
			
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
	
	public void onUplDapus(UploadEvent event){
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
					onSave(BTN_DAPUS, mediaName);
				}
			});
			
			lbl_dapus.setValue(mediaName);
			
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
	
	public void onUplLampiran(UploadEvent event){
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
					onSave(BTN_LAMP, mediaName);
				}
			});
			
			lbl_lampiran.setValue(mediaName);
			
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
	
	
	public void onSave(int idButton,String value){
		loadDatabase();
		
		switch(idButton){
		case BTN_AWAL :
			pkl.setDataAwal(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_BAB1 :
			pkl.setDataBab1(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_BAB2 :
			pkl.setDataBab2(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_BAB3 :
			pkl.setDataBab3(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_BAB4 :
			pkl.setDataBab4(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_BAB5 :
			pkl.setDataBab5(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_DAPUS :
			pkl.setDaftarPustaka(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
		case BTN_LAMP :
			pkl.setLampiran(value);
			sess.update(pkl);
			sess.getTransaction().commit();
			sess.close();
		break;
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
	
	public void onDownloadBab2(){
		try {
			download(BTN_AWAL);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onDownloadBab3(){
		try {
			download(BTN_AWAL);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDownloadBab4(){
		try {
			download(BTN_AWAL);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDownloadBab5(){
		try {
			download(BTN_AWAL);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDownloadDapus(){
		try {
			download(BTN_AWAL);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDownloadLampiran(){
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
			file = new File(path+"\\"+pkl.getDataAwal());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_BAB1:
			file = new File(path+"\\"+pkl.getDataBab1());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_BAB2:
			file = new File(path+"\\"+pkl.getDataBab2());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_BAB3:
			file = new File(path+"\\"+pkl.getDataBab3());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_BAB4:
			file = new File(path+"\\"+pkl.getDataBab4());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_BAB5:
			file = new File(path+"\\"+pkl.getDataBab5());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_DAPUS:
			file = new File(path+"\\"+pkl.getDaftarPustaka());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		case BTN_LAMP:
			file = new File(path+"\\"+pkl.getLampiran());
			Filedownload.save(file,null);
			System.out.println("Sukses");
		break;
		
		}
		
	}
}
