package skripsi.sitpast.act.mhs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Skripsi;

public class DataSkripsiAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	
	Textbox judul;
	Session sess;
	Mahasiswa maha;
	Skripsi skripsi;
	Button btn_simpan;
	Button btn_edit;
	int statusInt = 6;
	
	Label lbl_pembimbing1;
	Label lbl_pembimbing2;
	Label lbl_tgl_mulai;
	Label lbl_tgl_selesai;
	Label lbl_tgl_sidang;
	Label lbl_tgl_seminar;
	Label lbl_persetujuan;
	Label lbl_judul;
	
	Date date;
	DateFormat dateFormat;
	
	private static final long serialVersionUID = 473221796859059230L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		loadData();
	}
	
	
	private void loadData(){
		Mahasiswa mahasiswa;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		maha = (Mahasiswa) session.getAttribute("mahasiswa");
		sess = HibernateUtil.getSessionFactory().openSession();
		mahasiswa = (Mahasiswa) sess.createQuery("from Mahasiswa where id="+maha.getId()).uniqueResult();
		System.out.println("maha nama : "+maha.getNama());
		
		if(mahasiswa!=null){
			skripsi = mahasiswa.getSkripsi();
			
			if(skripsi!=null){
				lbl_judul.setValue(skripsi.getJudulSkripsi()!=null?skripsi.getJudulSkripsi():"");
				
				if(skripsi.getDosPemSatu()!=null)
				lbl_pembimbing1.setValue(skripsi.getDosPemSatu().getNama()!=null?skripsi.getDosPemSatu().getNama():""+" , "+skripsi.getDosPemSatu().getGelar()!=null?skripsi.getDosPemSatu().getGelar():"");
				
				if(skripsi.getDosPemDua()!=null)	
				lbl_pembimbing2.setValue(skripsi.getDosPemDua().getNama()!=null?skripsi.getDosPemDua().getNama():""+" , "+skripsi.getDosPemDua().getGelar()!=null?skripsi.getDosPemDua().getGelar():"");
				if(skripsi!=null)
				if(skripsi.getStatus()==0)
					lbl_persetujuan.setValue("Belum disetujui");
				else if(skripsi.getStatus()==1)
					lbl_persetujuan.setValue("Aktif/Disetujui");
				else if(skripsi.getStatus()==2)
					lbl_persetujuan.setValue("Lulus");
				else if(skripsi.getStatus()==3)
					lbl_persetujuan.setValue("Ditolak");
				
				lbl_tgl_mulai.setValue(skripsi.getTanggalMulai()!=null?dateFormat.format(skripsi.getTanggalMulai()):"");
				lbl_tgl_selesai.setValue(skripsi.getTanggalSelesai()!=null?dateFormat.format(skripsi.getTanggalSelesai()):"");
				lbl_tgl_sidang.setValue(skripsi.getTanggalSidang()!=null?dateFormat.format(skripsi.getTanggalSidang()):"");
				lbl_tgl_seminar.setValue(skripsi.getTanggalSeminar()!=null?dateFormat.format(skripsi.getTanggalSeminar()):"");
					
			}
				
			maha = mahasiswa;
			sess.disconnect();
			sess.close();
		}
		
	}
	
	
}
