package skripsi.sitpast.act.dosen;

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

import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;

public class DosenProfilAct extends GenericForwardComposer<Component>{

	
	Dosen dosen;
	Session sess;
	Textbox txt_nama;
	Textbox txt_alamat;
	Textbox txt_email;
	Textbox txt_gelar;
	Textbox txt_username;
	Textbox txt_password;
	Textbox txt_password_konf;
	Textbox txt_nip;
	Textbox txt_notelp;
	Label lbl_prodi;
	Button btn_edit;
	Button btn_simpan;
	String passLama;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		dosen = (Dosen) session.getAttribute("dosen");
		loadData();
	}

	private void loadData(){
		txt_nama.setText(dosen.getNama());
		txt_alamat.setText(dosen.getAlamat()==null?"":dosen.getAlamat());
		txt_email.setText(dosen.getEmail()==null?"":dosen.getEmail());
		txt_gelar.setText(dosen.getGelar()==null?"":dosen.getGelar());
		txt_username.setText(dosen.getUsername()==null?"":dosen.getUsername());
		txt_email.setText(dosen.getEmail()==null?"":dosen.getEmail());
		txt_password.setText(dosen.getPassword()==null?"":dosen.getPassword());
		txt_nip.setText(dosen.getNip()==null?"":dosen.getNip());
		lbl_prodi.setValue(dosen.getProdi().getNamaProdi());
		passLama = dosen.getPassword();
		
	}
	
	public void onEdit(){
		
		txt_nama.setReadonly(false);
		txt_alamat.setReadonly(false);
		txt_email.setReadonly(false);
		txt_gelar.setReadonly(false);
		txt_username.setReadonly(false);
		txt_password.setReadonly(false);
		txt_password_konf.setReadonly(false);
		txt_nip.setReadonly(false);
		txt_notelp.setReadonly(false);
		btn_edit.setVisible(false);
		btn_simpan.setVisible(true);

	}
	
	public void onSave(){
		Mahasiswa mahasiswa;
		sess = HibernateUtil.getSessionFactory().openSession();
		
		sess.beginTransaction();
		Dosen dataDosen = (Dosen) sess.createCriteria(Dosen.class).add(Restrictions.eq("id", dosen.getId())).uniqueResult();
		
		
		if(!txt_password.getValue().equals(passLama)){
			if(!txt_password_konf.getValue().equals(txt_password.getValue())){
				Messagebox.show("Password Konfirmasi harus diisi dan/atau sama");
				return;
			}
		}
		
		
		dataDosen.setAlamat(txt_alamat.getValue());
		dataDosen.setEmail(txt_email.getValue());
		dataDosen.setGelar(txt_gelar.getValue());
		dataDosen.setNama(txt_nama.getValue());
		dataDosen.setNip(txt_nip.getValue());
		dataDosen.setNoTelp(txt_notelp.getValue());
		dataDosen.setPassword(txt_password.getValue());
		
				
		sess.saveOrUpdate(dataDosen);
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
