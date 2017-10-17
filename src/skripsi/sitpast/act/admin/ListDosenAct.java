package skripsi.sitpast.act.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.ast.util.SessionFactoryHelper;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
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
import org.zkoss.zul.ItemRenderer;
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



import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Prodi;

public class ListDosenAct extends GenericForwardComposer{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8341892894443849235L;
	// Data
	AnnotateDataBinder binder;
	List<Dosen> listDosen;
	Dosen dosen;
	Session sess;
	ListModel model;
	ListModel modelProdi;
	List<Prodi> listProdi;
	Textbox txt_cari;
	
	//Component
	Listbox list_dosen;
	Window window;
	Textbox txt_nama;
	Textbox txt_nip;
	Textbox txt_alamat;
	Textbox txt_noTelp;
	Textbox txt_email;
	Textbox txt_gelar;
	Textbox txt_username;
	Textbox txt_password;
	Button btn_simpan;
	Button btn_batal;
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
	
	
	
	public void onDelete(Dosen data){
		
		final Dosen dosen = data;
		
		Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
			int i = new Integer(event.getData().toString());
			
				if(i == Messagebox.OK)
					deleteDosen(dosen);
				
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
	
	
	private void onSave(Dosen data){
		
		Dosen dosen = new Dosen();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			dosen.setId(data.getId());
		
		dosen.setNama(txt_nama.getText());
		dosen.setAlamat(txt_alamat.getText());
		dosen.setNip(txt_nip.getText());
		dosen.setGelar(txt_gelar.getText());
		dosen.setNoTelp(txt_noTelp.getText());
		dosen.setEmail(txt_email.getText());
		dosen.setProdi((Prodi) combo_prodi.getSelectedItem().getValue());
		dosen.setPassword(txt_password.getText());
		dosen.setUsername(txt_username.getText());
		
		
		sess.saveOrUpdate(dosen);
		sess.getTransaction().commit();
		
		Messagebox.show("Berhasil disimpan", "Simpan data", Messagebox.OK, null, new EventListener() {
			
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

	private void showForm(Dosen data){
		loadProdi();
		
		//data
		final Dosen dosen;
		
		if(data!=null)
			dosen = data;
		else
			dosen = new Dosen();
		
		
		//element
		window = new Window();
		window.setParent(self);
		window.setTitle("Form Dosen");
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
		txt_nama = new Textbox(dosen.getNama()==null?"":dosen.getNama());
		txt_nama.setWidth(txtBoxWidth);
		txt_nama.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("NIP"));
		txt_nip = new Textbox(dosen.getNip()==null?"":dosen.getNip());
		txt_nip.setWidth(txtBoxWidth);
		txt_nip.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Username"));
		txt_username = new Textbox(dosen.getUsername()==null?"":dosen.getUsername());
		txt_username.setWidth(txtBoxWidth);
		txt_username.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Password"));
		txt_password = new Textbox(dosen.getPassword()==null?"":dosen.getPassword());
		txt_password.setType("password");
		txt_password.setWidth(txtBoxWidth);
		txt_password.setParent(row);
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Gelar"));
		txt_gelar = new Textbox(dosen.getGelar()==null?"":dosen.getGelar());
		txt_gelar.setWidth(txtBoxWidth);
		txt_gelar.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Prodi"));
		Comboitem item;
		
		
		if(dosen.getProdi()!=null){
			System.out.println("ada prodi");
			item = new Comboitem();
			item.setLabel(dosen.getProdi().getNamaProdi());
			item.setValue(dosen.getProdi());
			combo_prodi.appendChild(item);
			System.out.println("Data prodi"+dosen.getProdi().getNamaProdi());
			
		}
	
		row.appendChild(combo_prodi);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Alamat"));
		txt_alamat = new Textbox(dosen.getAlamat()==null?"":dosen.getAlamat());
		txt_alamat.setWidth(txtBoxWidth);
		txt_alamat.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("No Telp"));
		txt_noTelp = new Textbox(dosen.getNoTelp()==null?"":dosen.getNoTelp());
		txt_noTelp.setWidth(txtBoxWidth);
		txt_noTelp.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Email"));
		txt_email = new Textbox(dosen.getEmail()==null?"":dosen.getEmail());
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
				onSave(dosen);
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
		listDosen = new ArrayList<Dosen>();
		
		Criteria criteria = sess.createCriteria(Dosen.class);
		Criterion rest1 = Restrictions.ilike("nama", dataQuery);
		Criterion rest2 = Restrictions.ilike("username", dataQuery);
		criteria.add(Restrictions.or(rest1,rest2));
//		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listDosen = criteria.list();
		model = new SimpleListModel<Dosen>(listDosen);
		list_dosen.setModel(model);
		list_dosen.setItemRenderer(new DosenRenderer());
		list_dosen.renderAll();	
		
		sess.close();
		
	}
	
	class DosenRenderer implements ListitemRenderer<Dosen>{

		int noUrut = 0;
		String namaString = "";
		String gelar = "";
		@Override
		public void render(Listitem item, Dosen data, int index)
				throws Exception {
			index = index + 1;
			
			final Dosen dosenTemp = data;
			// TODO Auto-generated method stub
			new Listcell(""+index).setParent(item);
			
			if(data.getNama()!=null)
				namaString = data.getNama();
			if(data.getGelar()!=null)
				gelar = data.getGelar();
			
			new Listcell(namaString + " , "+gelar).setParent(item);
			new Listcell(data.getNip()).setParent(item);
			new Listcell(data.getProdi()!=null?data.getProdi().getNamaProdi():"").setParent(item);
			new Listcell(data.getNoTelp()).setParent(item);
			new Listcell(data.getEmail()).setParent(item);
			new Listcell(data.getAlamat()).setParent(item);
			
			Hbox box = new Hbox();
			Button btnEdit = new Button("Edit");
			btnEdit.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showForm(dosenTemp);
				}
			
			
			});
			
			Button btnDelete = new Button("Hapus");
			btnDelete.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDelete(dosenTemp);
					
				}
			});
		
			btnEdit.setParent(box);
			btnDelete.setParent(box);
			Listcell listcell = new Listcell();
			listcell.appendChild(box);
			listcell.setParent(item);
			
		}	
		
	}
	
	
	private void deleteDosen(Dosen dosen){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.delete(dosen);
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
