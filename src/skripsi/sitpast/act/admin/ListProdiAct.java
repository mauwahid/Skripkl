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
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
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
import skripsi.sitpast.domain.Prodi;

public class ListProdiAct extends GenericForwardComposer<Component>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1395747346486792704L;

	//Component
	Listbox list_prodi;
	Window window;
	Textbox txt_nama;
	Textbox txt_keterangan;
	String txtBoxWidth = "200px";
	Button btn_simpan;
	Button btn_batal;
		
	//Data
	List<Prodi> listProdi;
	Session sess;
	ListModel<Prodi> model;
	Textbox txt_cari;
	


	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		loadData("");
	}
	
	public void onAdd(){
		showForm(null);
	}
	
	
	
	public void onDelete(Prodi data){
		
		final Prodi prodi = data;
		
		Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
			int i = new Integer(event.getData().toString());
			
				if(i == Messagebox.OK)
					deleteProdi(prodi);
				else
					return;
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
	
	
	private void onSave(Prodi data){
		
		Prodi prodi = new Prodi();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			prodi.setId(data.getId());
		
		prodi.setNamaProdi(txt_nama.getText());
		prodi.setKeterangan(txt_keterangan.getText());
		
		sess.saveOrUpdate(prodi);
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

	
	private void showForm(Prodi data){
		
		
		//data
		final Prodi prodi;
		
		if(data!=null)
			prodi = data;
		else
			prodi = new Prodi();
		
		
		//element
		window = new Window();
		window.setParent(self);
		window.setTitle("Form Prodi");
		window.setMode("popup");
		window.setPosition("center,top");
		window.setClosable(true);
		window.setWidth("500px");
		window.setHeight("300px");
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
		row.appendChild(new Label("Nama Prodi"));
		txt_nama = new Textbox(prodi.getNamaProdi()==null?"":prodi.getNamaProdi());
		txt_nama.setWidth(txtBoxWidth);
		txt_nama.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Keterangan"));
		txt_keterangan = new Textbox(prodi.getKeterangan()==null?"":prodi.getKeterangan());
		txt_keterangan.setWidth(txtBoxWidth);
		txt_keterangan.setParent(row);
		
		
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
				onSave(prodi);
			}
		
		});
		
		btn_batal.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				
				window.onClose();
			}
		
		});
		
		window.onModal();
	}
	
	private void loadData(String query){
		
		String dataQuery = "%"+query+"%";
		sess = HibernateUtil.getSessionFactory().openSession();
		listProdi = new ArrayList<Prodi>();
		
		Criteria criteria = sess.createCriteria(Prodi.class);
		Criterion rest1 = Restrictions.ilike("namaProdi", dataQuery);
		Criterion rest2 = Restrictions.ilike("keterangan", dataQuery);
		criteria.add(Restrictions.or(rest1,rest2));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listProdi = criteria.list();
		model = new SimpleListModel<Prodi>(listProdi);
		list_prodi.setModel(model);
		list_prodi.setItemRenderer(new ProdiRenderer());
		list_prodi.renderAll();	
		
		sess.close();
	}
	
	class ProdiRenderer implements ListitemRenderer<Prodi>{

		int noUrut = 0;
		
		@Override
		public void render(Listitem item, Prodi data, int index)
				throws Exception {
			index = index + 1;
			
			final Prodi prodiTemp = data;
			// TODO Auto-generated method stub
			new Listcell(""+index).setParent(item);
			new Listcell(data.getNamaProdi()).setParent(item);
			new Listcell(data.getKeterangan()).setParent(item);
			
			Hbox box = new Hbox();
			Button btnEdit = new Button("Edit");
			btnEdit.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showForm(prodiTemp);
				}
			
			
			});
			
			Button btnDelete = new Button("Hapus");
			btnDelete.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDelete(prodiTemp);
					
				}
			});
		
			btnEdit.setParent(box);
			btnDelete.setParent(box);
			Listcell listcell = new Listcell();
			listcell.appendChild(box);
			listcell.setParent(item);
			
		}	
		
	}
	
	
	private void deleteProdi(Prodi prodi){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.delete(prodi);
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
	
	
	
}
