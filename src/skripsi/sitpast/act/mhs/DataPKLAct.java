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
import skripsi.sitpast.domain.PKL;

public class DataPKLAct extends GenericForwardComposer<Component> {

	Textbox judul;
	Session sess;
	Mahasiswa maha;
	PKL pkl;
	Button btn_simpan;
	Button btn_edit;
	int statusInt = 6;
	
	Label lbl_pembimbing;
	Label lbl_tgl_mulai;
	Label lbl_tempat;
	Label lbl_tgl_selesai;
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
			pkl = mahasiswa.getPkl();
			
			if(pkl!=null){
				lbl_judul.setValue(pkl.getJudul());
				if(pkl.getDospemPKL()!=null)
				lbl_pembimbing.setValue(pkl.getDospemPKL().getNama()!=null?pkl.getDospemPKL().getNama():""+" , "+pkl.getDospemPKL().getGelar()!=null?pkl.getDospemPKL().getGelar():"");
				if(pkl.getStatus()==0)
					lbl_persetujuan.setValue("Belum disetujui");
				else if(pkl.getStatus()==1)
					lbl_persetujuan.setValue("Aktif/Disetujui");
				else if(pkl.getStatus()==2)
					lbl_persetujuan.setValue("Lulus");
				else if(pkl.getStatus()==3)
					lbl_persetujuan.setValue("Ditolak");
				
				lbl_tempat.setValue(pkl.getTempatPKL()!=null?pkl.getTempatPKL():"");
				lbl_tgl_mulai.setValue(pkl.getTanggalMulai()!=null?dateFormat.format(pkl.getTanggalMulai()):"");
				lbl_tgl_selesai.setValue(pkl.getTanggalAkhir()!=null?dateFormat.format(pkl.getTanggalAkhir()):"");
					
			}
				
			maha = mahasiswa;
			sess.disconnect();
			sess.close();
		}
		
	}
	
	

	
}
