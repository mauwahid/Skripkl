package skripsi.sitpast.act.admin;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import skripsi.sitpast.domain.UserTable;
import skripsi.sitpast.domain.Prodi;

public class ListUserAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -673795910022831078L;
	List<UserTable> listUserTable;
	UserTable user;
	Session sess;
	ListModel<UserTable> model;
	ListModel<Prodi> modelProdi;
	List<Prodi> listProdi;
	Textbox txt_cari;
	
	//Component
	Listbox list_user;
	Window window;
	Textbox txt_nama;
	Textbox txt_nip;
	Textbox txt_alamat;
	Textbox txt_noTelp;
	Textbox txt_email;
	Textbox txt_username;
	Textbox txt_password;
	Button btn_simpan;
	Button btn_batal;
	Combobox combo_status;
	Combobox combo_prodi;
	String txtBoxWidth = "200px";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		loadData("");
	}
	
	public void onAdd(){
		showForm(null);
	}
	
	
	
	public void onDelete(UserTable data){
		
		final UserTable user = data;
		
		Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
			int i = new Integer(event.getData().toString());
			
				if(i == Messagebox.OK)
					deleteUserTable(user);
				
			}
		});
		
	}
	
	public void onSearch(){
		String query = txt_cari.getText();
		loadData(query);
	}
	
	public void onRefresh(){
		loadData("");
	}
	
	
	private void onSave(UserTable data){
		
		UserTable user = new UserTable();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			if(data.getId()!=null)
				user.setId(data.getId());
		
		user.setNamaLengkap(txt_nama.getText());
		user.setAlamat(txt_alamat.getText());
		user.setNip(txt_nip.getText());
		int status = 0;
		switch(combo_status.getSelectedIndex()){
		case 0 :
			status = 1;
			break;
		case 1 :
			status = 2;
			user.setProdi((Prodi) combo_prodi.getSelectedItem().getValue());
			break;
		case 3 :
			status = 3;
			break;
		
		}
		user.setStatus(status);
		user.setNoTelp(txt_noTelp.getText());
		user.setEmail(txt_email.getText());
		user.setPassword(txt_password.getText());
		user.setUsername(txt_username.getText());
		
		
		sess.saveOrUpdate(user);
		sess.getTransaction().commit();
		
		Messagebox.show("Berhasil disimpan", "Simpan data", Messagebox.OK, null, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				window.onClose();
			}
		});
		
		
		sess.disconnect();
		sess.close();

		loadData("");
		
	}

	private void showForm(UserTable data){
		loadProdi();
		
		
		Comboitem itemProdi;
		
		//data
		final UserTable user;
		
		if(data!=null)
			user = data;
		else
			user = new UserTable();
		
		
		//element
		window = new Window();
		window.setParent(self);
		window.setTitle("Form User");
		window.setMode("popup");
		window.setPosition("center,top");
		window.setClosable(true);
		window.setWidth("500px");
		window.setHeight("500px");
		window.setVisible(true);
		
		Grid grid = new Grid();
		Columns columns = new Columns();
		Rows rows = new Rows();
		rows.setParent(grid);
		
		Column column;
		Row row;
		Textbox textbox;
		Label label;
		
		grid.setParent(window);
		columns.setParent(grid);
	
		column = new Column();
		column.setWidth("35%");
		column.setParent(columns);
		
		column = new Column();
		column.setParent(columns);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nama"));
		txt_nama = new Textbox(user.getNamaLengkap()==null?"":user.getNamaLengkap());
		txt_nama.setWidth(txtBoxWidth);
		txt_nama.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("NIP"));
		txt_nip = new Textbox(user.getNip()==null?"":user.getNip());
		txt_nip.setWidth(txtBoxWidth);
		txt_nip.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Username"));
		txt_username = new Textbox(user.getUsername()==null?"":user.getUsername());
		txt_username.setWidth(txtBoxWidth);
		txt_username.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Password"));
		txt_password = new Textbox(user.getPassword()==null?"":user.getPassword());
		txt_password.setType("password");
		txt_password.setWidth(txtBoxWidth);
		txt_password.setParent(row);
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Status"));
		combo_status = new Combobox();
		combo_status.setReadonly(true);
		Comboitem item;
		item = new Comboitem("Admin");
		item.setParent(combo_status);
		item = new Comboitem("Prodi");
		item.setParent(combo_status);
		item = new Comboitem("Pudek");
		item.setParent(combo_status);

		combo_status.addEventListener("onChange", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				if(combo_status.getSelectedIndex()==1)
					combo_prodi.setVisible(true);
				else
					combo_prodi.setVisible(false);
			}
		
		
		});
		combo_status.setParent(row);
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Prodi"));
		
		combo_prodi.setParent(row);
		combo_prodi.setVisible(false);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Alamat"));
		txt_alamat = new Textbox(user.getAlamat()==null?"":user.getAlamat());
		txt_alamat.setWidth(txtBoxWidth);
		txt_alamat.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("No Telp"));
		txt_noTelp = new Textbox(user.getNoTelp()==null?"":user.getNoTelp());
		txt_noTelp.setWidth(txtBoxWidth);
		txt_noTelp.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Email"));
		txt_email = new Textbox(user.getEmail()==null?"":user.getEmail());
		txt_email.setWidth(txtBoxWidth);
		txt_email.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Aksi"));
		Hlayout hlay = new Hlayout();
		hlay.appendChild(btn_simpan = new Button("Simpan"));
		hlay.appendChild(btn_batal = new Button("Batal"));
		row.appendChild(hlay);
		
		btn_simpan.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				onSave(user);
			}
		
		});
		
		window.onModal();
		
		btn_batal.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				
				window.onClose();
			}
		
		});
		
	}
	
	
	
	private void loadData(String query){
		
		String dataQuery = "%"+query+"%";
		sess = HibernateUtil.getSessionFactory().openSession();
		listUserTable = new ArrayList<UserTable>();
		
		Criteria criteria = sess.createCriteria(UserTable.class);
		Criterion rest1 = Restrictions.ilike("namaLengkap", dataQuery);
		Criterion rest2 = Restrictions.ilike("username", dataQuery);
		criteria.add(Restrictions.or(rest1,rest2));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listUserTable = criteria.list();
		model = new SimpleListModel<UserTable>(listUserTable);
		list_user.setModel(model);
		list_user.setItemRenderer(new UserTableRenderer());
		list_user.renderAll();	
		
		sess.close();
		
	}
	
	class UserTableRenderer implements ListitemRenderer<UserTable>{

		int noUrut = 0;
		String status = "";
		
		@Override
		public void render(Listitem item, UserTable data, int index)
				throws Exception {
			index = index + 1;
			
			final UserTable userTemp = data;
			// TODO Auto-generated method stub
			new Listcell(""+index).setParent(item);
			new Listcell(data.getNamaLengkap()).setParent(item);
			new Listcell(data.getUsername()).setParent(item);
			new Listcell(data.getNip()).setParent(item);
			if(data.getStatus()!=0){
				switch(data.getStatus()){
				case 1 :
					status = "Admin";
					break;
				case 2 :
					status = "Prodi";
					break;
				case 3 :
					status = "Pudek";
					break;
				default :
					status = "";
					break;
				
				}
			}
			
			new Listcell(status).setParent(item);
			new Listcell(data.getNoTelp()).setParent(item);
			new Listcell(data.getEmail()).setParent(item);
			new Listcell(data.getAlamat()).setParent(item);
			
			Hbox box = new Hbox();
			Button btnEdit = new Button("Edit");
			btnEdit.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showForm(userTemp);
				}
			
			
			});
			
			Button btnDelete = new Button("Hapus");
			btnDelete.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDelete(userTemp);
					
				}
			});
		
			btnEdit.setParent(box);
			btnDelete.setParent(box);
			Listcell listcell = new Listcell();
			listcell.appendChild(box);
			listcell.setParent(item);
			
		}	
		
	}
	
	
	private void deleteUserTable(UserTable user){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.delete(user);
		sess.getTransaction().commit();
		sess.disconnect();
		sess.close();
		
		Messagebox.show("Berhasil dihapus", "Hapus data", Messagebox.OK, null, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				loadData("");
				
			}
		});
		
	}
	
	private void loadProdi(){
		combo_prodi = new Combobox();
		combo_prodi.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listProdi = new ArrayList<Prodi>();
		listProdi = sess.createCriteria(Prodi.class).list();
		modelProdi = new SimpleListModel<Prodi>(listProdi);
		
		ListModelList<Prodi> listModel = new ListModelList<Prodi>(listProdi);
		listModel.addSelection(listModel.get(0));
		
		combo_prodi.setModel(listModel);
		combo_prodi.setItemRenderer(new ProdiRenderer());
		combo_prodi.getItemRenderer();
		
		
		System.out.println("render selesai");
	}
	
	class ProdiRenderer implements ComboitemRenderer<Prodi>{

		@Override
		public void render(Comboitem item, Prodi data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			item.setValue(data);
			item.setLabel(data.getNamaProdi());
			System.out.println("data : "+data.getNamaProdi());
			
		}
		
	}
	
}
