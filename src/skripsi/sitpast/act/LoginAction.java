package skripsi.sitpast.act;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.UserTable;

public class LoginAction extends GenericAutowireComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9053720665643389330L;
	Textbox text_username;
	Textbox text_password;
	Combobox combo_status;
	Label text_confirm_status;
	Label text_confirm;
	boolean status = false;
	
	String username;
	String password;
	
	Session sess;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		
	}
	
	
	public void onCheckLogin(){
		Session sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		checkMahasiswa();
		checkDosen();
		checkProdi();
		checkAdmin();
		
		showConfirm();
	}

	private void getUserPass(){
		username = text_username.getText();
		password = text_password.getText();
	}
	

	private void checkPudek() {
		// TODO Auto-generated method stub
		getUserPass();
		sess = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = sess.createCriteria(UserTable.class);
		Criterion rest1 = Restrictions.like("username", username);
		Criterion rest2 = Restrictions.like("password", password);
		Criterion rest3 = Restrictions.eq("status", 3);
		Criterion restAdd = Restrictions.and(rest1,rest2);
		criteria.add(Restrictions.and(restAdd,rest3));
		UserTable user = (UserTable) criteria.uniqueResult();
		
		if(user==null)
			showConfirm();
		else{
			session.setAttribute("user", user);
			Executions.sendRedirect("./pudek/main.zul");
		}
		
		
	}


	private void checkProdi() {
		// TODO Auto-generated method stub
		getUserPass();
		sess = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = sess.createCriteria(UserTable.class);
		Criterion rest1 = Restrictions.like("username", username);
		Criterion rest2 = Restrictions.like("password", password);
		Criterion rest3 = Restrictions.eq("status", 2);
		Criterion restAdd = Restrictions.and(rest1,rest2);
		criteria.add(Restrictions.and(restAdd,rest3));
		UserTable user = (UserTable) criteria.uniqueResult();
		
		if(user==null)
			return;
		else{
			session.setAttribute("user", user);
			Executions.sendRedirect("./prodi/main.zul");
		}
	}


	private void checkAdmin() {
		// TODO Auto-generated method stub
		getUserPass();
		sess = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = sess.createCriteria(UserTable.class);
		Criterion rest1 = Restrictions.like("username", username);
		Criterion rest2 = Restrictions.like("password", password);
		Criterion rest3 = Restrictions.eq("status", 1);
		Criterion restAdd = Restrictions.and(rest1,rest2);
		criteria.add(Restrictions.and(restAdd,rest3));
		UserTable user = (UserTable) criteria.uniqueResult();
		
		if(user==null)
			return;
		else{
			session.setAttribute("user", user);
			Executions.sendRedirect("./admin/main.zul");
		}
	}


	private void checkDosen() {
		// TODO Auto-generated method stub
		getUserPass();
		sess = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = sess.createCriteria(Dosen.class);
		Criterion rest1 = Restrictions.like("username", username);
		Criterion rest2 = Restrictions.like("password", password);
		criteria.add(Restrictions.and(rest1,rest2));
		Dosen dosen = (Dosen) criteria.uniqueResult();
		
		if(dosen==null)
			return;
		else{
			session.setAttribute("dosen", dosen);
			Executions.sendRedirect("./dosen/main.zul");
		}
			
	}


	private void checkMahasiswa() {
		// TODO Auto-generated method stub
		getUserPass();
		sess = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = sess.createCriteria(Mahasiswa.class);
		Criterion rest1 = Restrictions.like("username", username);
		Criterion rest2 = Restrictions.like("password", password);
		criteria.add(Restrictions.and(rest1,rest2));
		Mahasiswa maha = (Mahasiswa) criteria.uniqueResult();
		
		if(maha==null)
			return;
		else{
			session.setAttribute("mahasiswa", maha);
			Executions.sendRedirect("./mahasiswa/main.zul");
		}
			
		
	}
	
	private void showConfirm(){
		text_password.setText("");
		text_username.setText("");
		text_confirm.setVisible(true);
		text_username.setFocus(true);
	}
	
	
	
}
