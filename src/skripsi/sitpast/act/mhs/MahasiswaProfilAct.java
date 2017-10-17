package skripsi.sitpast.act.mhs;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;

public class MahasiswaProfilAct extends GenericForwardComposer<Component>{

	
	Mahasiswa mahasiswa;
	Session sess;
	Textbox txt_nama;
	Textbox txt_alamat;
	Textbox txt_email;
	Textbox txt_username;
	Textbox txt_password;
	Textbox txt_password_konf;
	Textbox txt_nim;
	Textbox txt_notelp;
	Label lbl_prodi;
	Button btn_edit;
	Button btn_simpan;
	String passLama;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		mahasiswa = (Mahasiswa) session.getAttribute("mahasiswa");
		loadData();
	}

	private void loadData(){
		txt_nama.setText(mahasiswa.getNama());
		txt_alamat.setText(mahasiswa.getAlamat()==null?"":mahasiswa.getAlamat());
		txt_email.setText(mahasiswa.getEmail()==null?"":mahasiswa.getEmail());
		txt_username.setText(mahasiswa.getUsername()==null?"":mahasiswa.getUsername());
		txt_email.setText(mahasiswa.getEmail()==null?"":mahasiswa.getEmail());
		txt_password.setText(mahasiswa.getPassword()==null?"":mahasiswa.getPassword());
		txt_nim.setText(mahasiswa.getNim()==null?"":mahasiswa.getNim());
		lbl_prodi.setValue(mahasiswa.getProdi().getNamaProdi());
		passLama = mahasiswa.getPassword();
		
	}
	
	public void onEdit(){
		
		txt_nama.setReadonly(false);
		txt_alamat.setReadonly(false);
		txt_email.setReadonly(false);
		txt_username.setReadonly(false);
		txt_password.setReadonly(false);
		txt_password_konf.setReadonly(false);
		txt_nim.setReadonly(false);
		txt_notelp.setReadonly(false);
		btn_edit.setVisible(false);
		btn_simpan.setVisible(true);

	}
	
	public void onSave(){
		sess = HibernateUtil.getSessionFactory().openSession();
		
		sess.beginTransaction();
		Mahasiswa dataMahasiswa = (Mahasiswa) sess.createCriteria(Mahasiswa.class).add(Restrictions.eq("id", mahasiswa.getId())).uniqueResult();
		
		
		if(!txt_password.getValue().equals(passLama)){
			if(!txt_password_konf.getValue().equals(txt_password.getValue())){
				Messagebox.show("Password Konfirmasi harus diisi dan/atau sama");
				return;
			}
		}
		
		
		dataMahasiswa.setAlamat(txt_alamat.getValue());
		dataMahasiswa.setEmail(txt_email.getValue());
		dataMahasiswa.setNama(txt_nama.getValue());
		dataMahasiswa.setNim(txt_nim.getValue());
		dataMahasiswa.setNoTelp(txt_notelp.getValue());
		dataMahasiswa.setPassword(txt_password.getValue());
		
				
		sess.saveOrUpdate(dataMahasiswa);
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
