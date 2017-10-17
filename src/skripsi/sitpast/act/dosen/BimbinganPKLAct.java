package skripsi.sitpast.act.dosen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
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
import org.zkoss.zul.Datebox;
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

import skripsi.sitpast.domain.BimbinganPKL;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.DosenPKL;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;

public class BimbinganPKLAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	//Object
	Dosen dosen;
	List<Mahasiswa> listMahasiswa;
	ListModel<Mahasiswa> modelMahasiswa;
	Session sess;
	Mahasiswa maha;
	List<BimbinganPKL> bimbinganPKL;
	DateFormat dateFormat;
	PKL pkl;
	Window window;
	Label judul;
	
	
	Listbox list_bimbingan_pkl;
	Combobox combo_mahasiswa;
	private SimpleListModel<BimbinganPKL> model;
	private Datebox tgl_bimbingan;
	private Textbox txt_tempat;
	private Textbox txt_catatan;
	private Button btn_simpan;
	private Button btn_batal;
	
	
	private static final long serialVersionUID = -5035856848781791577L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		dosen = (Dosen) session.getAttribute("dosen");
		loadComboMhs();
	}

	private void showForm(BimbinganPKL data){
		//	loadProdi();
			
			//data
			final BimbinganPKL bimbingan;
			
			if(data!=null)
				bimbingan = data;
			else
				bimbingan = new BimbinganPKL();
			
			//element
			window = new Window();
			window.setContentStyle("overflow:auto");
			
			window.setParent(self);
			window.setTitle("Form BimbinganPKL");
			window.setMode("popup");
			window.setPosition("center,top");
			window.setClosable(true);
			window.setWidth("400px");
			window.setHeight("400px");
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
			row.appendChild(new Label("Tanggal Bimbingan"));
			tgl_bimbingan = new Datebox(bimbingan.getTglBimbingan()==null?null:bimbingan.getTglBimbingan());
			tgl_bimbingan.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Tempat"));
			txt_tempat = new Textbox(bimbingan.getTempat()==null?"":bimbingan.getTempat());
			txt_tempat.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Catatan"));
			txt_catatan = new Textbox(bimbingan.getCatatan()==null?"":bimbingan.getCatatan());
			txt_catatan.setWidth("300px");
			txt_catatan.setHeight("200px");
			txt_catatan.setMultiline(true);
			txt_catatan.setParent(row);
			
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
					onSave(bimbingan);
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
	
		
	private void onSave(BimbinganPKL data){
		
		BimbinganPKL bimbingan = new BimbinganPKL();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			bimbingan.setId(data.getId());
		
		bimbingan.setTglBimbingan(tgl_bimbingan.getValue());
		bimbingan.setCatatan(txt_catatan.getValue());
		bimbingan.setTempat(txt_tempat.getValue());
		bimbingan.setPkl(maha.getPkl());
		
		sess.saveOrUpdate(bimbingan);
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

		onLoadPKL();
	}


	private void loadComboMhs(){
	
		combo_mahasiswa.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listMahasiswa = new ArrayList<Mahasiswa>();
		Mahasiswa mhs;
		
		
		Iterator<PKL> iter = dosen.getDospemPKL().iterator();
		while(iter.hasNext()){
			mhs = iter.next().getMahasiswa();
			System.out.println("Nama MHs : "+mhs.getNama());
			listMahasiswa.add(mhs);
		}
		
	//	modelMahasiswa = new SimpleListModel<Mahasiswa>(listMahasiswa);
		ListModelList<Mahasiswa> listModel = new ListModelList<Mahasiswa>(listMahasiswa);
		
		combo_mahasiswa.setModel(listModel);
		combo_mahasiswa.setItemRenderer(new MhsRenderer());
		combo_mahasiswa.getItemRenderer();
		
		
		System.out.println("render selesai");
	}
	
	
	private class MhsRenderer implements ComboitemRenderer<Mahasiswa>{

		@Override
		public void render(Comboitem item, Mahasiswa data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			item.setValue(data);
			item.setLabel(data.getNama());
			System.out.println("data : "+data.getNama());
			
		}
		
	}
	
	
	public void onLoadPKL(){
		maha = (Mahasiswa) combo_mahasiswa.getSelectedItem().getValue();
		showDataPKL(maha);
	}
	
	
	private void showDataPKL(Mahasiswa data){
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		sess = HibernateUtil.getSessionFactory().openSession();
		bimbinganPKL = new ArrayList<BimbinganPKL>();
		
		Criteria criteria = sess.createCriteria(BimbinganPKL.class);
		criteria.add(Restrictions.eq("pkl", data.getPkl()));
		
		
		bimbinganPKL = criteria.list();
		model = new SimpleListModel<BimbinganPKL>(bimbinganPKL);
		list_bimbingan_pkl.setModel(model);
		list_bimbingan_pkl.setItemRenderer(new BimbinganPKLRenderer());
		list_bimbingan_pkl.renderAll();	
		
		judul.setValue(data.getPkl().getJudul());
		sess.close();
		
	}
	
	
	private void loadData(String query){
		
	}
	
	public void onAdd(){
		showForm(null);
	}
	

	class BimbinganPKLRenderer implements ListitemRenderer<BimbinganPKL>{
		int index = 0;
		
		@Override
		public void render(Listitem item, BimbinganPKL data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			final BimbinganPKL bimbinganTemp = data;
			
			index = index + 1;
			new Listcell(index+"").setParent(item);
			new Listcell(dateFormat.format(data.getTglBimbingan())).setParent(item);
			new Listcell(data.getPkl().getMahasiswa().getNama()==null?null:data.getPkl().getMahasiswa().getNama()).setParent(item);
			new Listcell(data.getTempat()).setParent(item);
			new Listcell(data.getCatatan()).setParent(item);
			
			Hbox box = new Hbox();
			Button btnEdit = new Button("Edit");
			btnEdit.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showForm(bimbinganTemp);
				}
			
			
			});
			
			Button btnDelete = new Button("Hapus");
			btnDelete.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDelete(bimbinganTemp);
					
				}
			});
		
			btnEdit.setParent(box);
			btnDelete.setParent(box);
			Listcell listcell = new Listcell();
			listcell.appendChild(box);
			listcell.setParent(item);
			
		}	
			
	}
	
	
	private void onDelete(BimbinganPKL bimbingan){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.delete(bimbingan);
		sess.getTransaction().commit();
		sess.disconnect();
		sess.close();
		
		Messagebox.show("Berhasil dihapus", "Hapus data", Messagebox.OK, null, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				onLoadPKL();
				
			}
		});
		
	}
		
}


