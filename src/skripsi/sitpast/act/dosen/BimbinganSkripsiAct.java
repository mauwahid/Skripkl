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

import skripsi.sitpast.domain.BimbinganSkripsi;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Skripsi;

public class BimbinganSkripsiAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	//Object
	Dosen dosen;
	List<Mahasiswa> listMahasiswa;
	ListModel<Mahasiswa> modelMahasiswa;
	Session sess;
	Mahasiswa maha;
	List<BimbinganSkripsi> bimbinganSkripsi;
	DateFormat dateFormat;
	Skripsi skripsi;
	Window window;
	Label judul;
	Combobox comboValid;
	
	
	Listbox list_bimbingan_skripsi;
	Combobox combo_mahasiswa;
	private SimpleListModel<BimbinganSkripsi> model;
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

	private void showForm(BimbinganSkripsi data){
		//	loadProdi();
			
			//data
			final BimbinganSkripsi bimbingan;
			
			if(data!=null)
				bimbingan = data;
			else
				bimbingan = new BimbinganSkripsi();
			
			//element
			window = new Window();
			window.setContentStyle("overflow:auto");
			
			window.setParent(self);
			window.setTitle("Form BimbinganSkripsi");
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
	
		
	private void onSave(BimbinganSkripsi data){
		
		BimbinganSkripsi bimbingan = new BimbinganSkripsi();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			bimbingan.setId(data.getId());
		
		bimbingan.setTglBimbingan(tgl_bimbingan.getValue());
		bimbingan.setCatatan(txt_catatan.getValue());
		bimbingan.setTempat(txt_tempat.getValue());
		bimbingan.setSkripsi(maha.getSkripsi());
		
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

		onLoadSkripsi();
	}


	private void loadComboMhs(){
	
		combo_mahasiswa.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listMahasiswa = new ArrayList<Mahasiswa>();
		Mahasiswa mhs;
		
		
		Iterator<Skripsi> iter = dosen.getDospemSatu().iterator();
		while(iter.hasNext()){
			mhs = iter.next().getMahasiswa();
			System.out.println("Nama MHs : "+mhs.getNama());
			listMahasiswa.add(mhs);
		}
		
		Iterator<Skripsi> iterDosen = dosen.getDospemDua().iterator();
		while(iterDosen.hasNext()){
			mhs = iterDosen.next().getMahasiswa();
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
			item.setLabel(data.getNama() + " ("+ data.getProdi().getNamaProdi()+ " - "+data.getNim()+ ")");
			System.out.println("data : "+data.getNama());
			
		}
		
	}
	
	
	public void onLoadSkripsi(){
		maha = (Mahasiswa) combo_mahasiswa.getSelectedItem().getValue();
		showDataSkripsi(maha);
	}
	
	
	public void onValidasi(){
		maha = (Mahasiswa) combo_mahasiswa.getSelectedItem().getValue();
		showValidasiForm(maha.getSkripsi());
	}
	
	private void showValidasiForm(Skripsi skripsi){
		final Skripsi dataSkripsi = skripsi;
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle(" Validasi Skripsi "+dataSkripsi.getMahasiswa().getNama());
		window.setMode("popup");
		window.setPosition("center,top");
		window.setClosable(true);
		window.setWidth("300px");
		window.setHeight("200px");
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
		row.appendChild(new Label("Validasi :"));
		comboValid = new Combobox();
		Comboitem comboProses = new Comboitem("Dalam Pengerjaan");
		Comboitem comboSelesai = new Comboitem("Telah Selesai/Valid");
		comboProses.setParent(comboValid);
		comboSelesai.setParent(comboValid);
		comboValid.setParent(row);
		
		if(dataSkripsi.getValidasi()==0){
			comboValid.setSelectedIndex(0);
		}else{
			comboValid.setSelectedIndex(1);
		}
	
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Aksi"));
		Button btn_batal = new Button("Simpan");
		btn_batal.setParent(row);
		
		window.onModal();
		
		btn_batal.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				onSaveValidasi(dataSkripsi);
				window.onClose();
			}
		
		});
		
				
	}
	
	private void onSaveValidasi(Skripsi data){
		
		
		sess = HibernateUtil.getSessionFactory().openSession();   
		sess.beginTransaction();
		
		Skripsi skripsi = (Skripsi)sess.createCriteria(Skripsi.class).add(Restrictions.eq("id", data.getId())).uniqueResult();
		
		
		if(comboValid.getSelectedIndex()==0)
			skripsi.setValidasi(0);
		else
			skripsi.setValidasi(1);
		
		sess.update(skripsi);
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
		loadComboMhs();	
	}

	
	
	
	private void showDataSkripsi(Mahasiswa data){
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		sess = HibernateUtil.getSessionFactory().openSession();
		bimbinganSkripsi = new ArrayList<BimbinganSkripsi>();
		
		Criteria criteria = sess.createCriteria(BimbinganSkripsi.class);
		criteria.add(Restrictions.eq("skripsi", data.getSkripsi()));
		
		bimbinganSkripsi = criteria.list();
		model = new SimpleListModel<BimbinganSkripsi>(bimbinganSkripsi);
		list_bimbingan_skripsi.setModel(model);
		list_bimbingan_skripsi.setItemRenderer(new BimbinganSkripsiRenderer());
		list_bimbingan_skripsi.renderAll();	
		
		String statusValidasi = "Dalam Proses Pengerjaan";
		
		if(data.getSkripsi().getValidasi()==1)
			statusValidasi = "Selesai/Lulus";
		
		judul.setValue(data.getSkripsi().getJudulSkripsi() + "  (Validasi : "+statusValidasi+" )");
		sess.close();
		
	}
	
	
	private void loadData(String query){
		
	}
	
	public void onAdd(){
		showForm(null);
	}
	

	class BimbinganSkripsiRenderer implements ListitemRenderer<BimbinganSkripsi>{
		int index = 0;
		
		
		@Override
		public void render(Listitem item, BimbinganSkripsi data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			final BimbinganSkripsi bimbinganTemp = data;
			
			index = index + 1;
			new Listcell(index+"").setParent(item);
			new Listcell(dateFormat.format(data.getTglBimbingan())).setParent(item);
			new Listcell(data.getSkripsi().getMahasiswa().getNama()==null?null:data.getSkripsi().getMahasiswa().getNama()).setParent(item);
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
	
	
	private void onDelete(BimbinganSkripsi bimbingan){
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
				onLoadSkripsi();
				
			}
		});
		
	}
		
}


