package skripsi.sitpast.act.mhs;

import org.hibernate.Session;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

public class FormMahasiswaAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6314421401143715006L;
	Textbox txt_mhs;
	Textbox txt_nim;
	Textbox txt_username;
	Textbox txt_password;
	Textbox txt_email;
	Textbox txt_angkatan;
	Textbox txt_notelp;
	Combobox combo_prodi;
	
	
	Session sess;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	
	public void onSave(){
		
	}
	
	public void onCancel(){
		
	}
	
	private void loadData(){
		
	}
	
	
	
	
}
