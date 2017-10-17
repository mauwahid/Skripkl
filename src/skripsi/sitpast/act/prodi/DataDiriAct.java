package skripsi.sitpast.act.prodi;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Textbox;

import skripsi.sitpast.domain.Prodi;
import skripsi.sitpast.domain.UserTable;

public class DataDiriAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -669046279814657358L;

	Session sess;
	Textbox nama;
	Textbox username;
	Textbox password;
	Textbox email;
	Textbox notelepon;
	Textbox nip;
	Textbox prodi;
	UserTable prodiObj;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		prodiObj = (UserTable) session.getAttribute("user");
		loadData();
	}
	
	
	private void loadData(){
		sess = HibernateUtil.getSessionFactory().openSession();
		UserTable data;
		Criteria criteria = sess.createCriteria(UserTable.class);
		Criterion crit = Restrictions.eq("id", prodiObj.getId());
		
		criteria.add(crit);
		data = (UserTable) criteria.uniqueResult();
		
		nama.setText(data.getNamaLengkap());
		username.setText(data.getUsername());
		password.setText(data.getPassword());
		password.setType("password");
		nip.setText(data.getNip());
		notelepon.setText(data.getNoTelp());
		email.setText(data.getEmail());
	}

}
