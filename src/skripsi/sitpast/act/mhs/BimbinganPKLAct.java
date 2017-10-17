package skripsi.sitpast.act.mhs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
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
import skripsi.sitpast.domain.BimbinganPKL;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Prodi;

public class BimbinganPKLAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5229477514347714569L;
	private DateFormat dateFormat;
	private Session sess;
	private List<BimbinganPKL> listBimbingan;
	private ListModel<BimbinganPKL> model;
	private Listbox list_bimbingan_pkl;
	private List<Dosen> listDosen;
	private ListModel<Dosen> modelDosen;
	private Mahasiswa maha;
	
	Datebox tgl_bimbingan;
	Window window;
	Textbox txt_catatan;
	Textbox txt_tempat;
	Combobox combo_dosen;
	Button btn_simpan;
	Button btn_batal;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		maha = (Mahasiswa) session.getAttribute("mahasiswa");
		loadData("");
	}
	
	
	private void loadData(String query){
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		String dataQuery = "%"+query+"%";
		sess = HibernateUtil.getSessionFactory().openSession();
		listBimbingan = new ArrayList<BimbinganPKL>();
		
		Criteria criteria = sess.createCriteria(BimbinganPKL.class);
		
		if(maha.getPkl()!=null)
		{
			Criterion crit = Restrictions.eq("pkl", maha.getPkl());
			criteria.add(crit);
		}
		
		listBimbingan = criteria.list();
		model = new SimpleListModel<BimbinganPKL>(listBimbingan);
		list_bimbingan_pkl.setModel(model);
		list_bimbingan_pkl.setItemRenderer(new BimbinganPKLRenderer());
		list_bimbingan_pkl.renderAll();	
		
		sess.close();
	}

	class BimbinganPKLRenderer implements ListitemRenderer<BimbinganPKL>{
		int index = 0;
		
		@Override
		public void render(Listitem item, BimbinganPKL data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			
			final BimbinganPKL bPKL = data;
			index = index + 1;
			new Listcell(index+"").setParent(item);
			new Listcell(dateFormat.format(data.getTglBimbingan())).setParent(item);
			new Listcell(data.getDospem()==null?null:data.getDospem().getNama()+" , "+data.getDospem().getGelar()).setParent(item);
			new Listcell(data.getTempat()).setParent(item);
			new Listcell(data.getCatatan()).setParent(item);
			
			Button btnView = new Button("Lihat");
			btnView.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showData(bPKL);
					
				}
			
			
			});
			
			Listcell lcell = new Listcell();
			btnView.setParent(lcell);
			lcell.setParent(item);
			
		}
		
	}
	
	public void onAdd(){
		
		if(maha.getPkl().getDospemPKL()==null){
			Messagebox.show("PKL Belum disetujui");
			return;
		}
			
		showForm(null);
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
		bimbingan.setDospem((Dosen)combo_dosen.getSelectedItem().getValue());
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

		loadData("");
		
	}

	
	private void showData(BimbinganPKL data){
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
		Label lbl_tgl_bimbingan = new Label (dateFormat.format(bimbingan.getTglBimbingan()));
		lbl_tgl_bimbingan.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tempat"));
		txt_tempat = new Textbox(bimbingan.getTempat()==null?"":bimbingan.getTempat());
		txt_tempat.setReadonly(true);
		txt_tempat.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Catatan"));
		txt_catatan = new Textbox(bimbingan.getCatatan()==null?"":bimbingan.getCatatan());
		txt_catatan.setWidth("300px");
		txt_catatan.setHeight("200px");
		txt_catatan.setReadonly(true);
		txt_catatan.setMultiline(true);
		txt_catatan.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing"));
		Label lbl_dospem = new Label(bimbingan.getDospem().getNama()+ " , "+bimbingan.getDospem().getGelar());
		lbl_dospem.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Aksi"));
		Hlayout hlay = new Hlayout();
		hlay.appendChild(btn_batal = new Button("OK"));
		row.appendChild(hlay);
		
		
		window.onModal();
		
		btn_batal.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				
				window.onClose();
			}
		
		});
		
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
				row.appendChild(new Label("Dosen Pembimbing"));
				loadDosen();
				combo_dosen.setParent(row);
				
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
	
	
public void onDelete(BimbinganPKL data){
		
		final BimbinganPKL bimbingan = data;
		
		Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
			int i = new Integer(event.getData().toString());
			
				if(i == Messagebox.OK)
					deleteBimbinganPKL(bimbingan);
				
			}
		});
		
	}
	
	
	private void deleteBimbinganPKL(BimbinganPKL bimbingan){
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
				loadData("");
				
			}
		});
		
	}
	
	/*private void loadProdi(){
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
*/	
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
	
	private void loadDosen(){
		combo_dosen = new Combobox();
		combo_dosen.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listDosen = new ArrayList<Dosen>();
		listDosen.add(maha.getPkl().getDospemPKL());
		
		modelDosen = new SimpleListModel<Dosen>(listDosen);
		
		ListModelList<Dosen> listModel = new ListModelList<Dosen>(listDosen);
		listModel.addSelection(listModel.get(0));
		
		combo_dosen.setModel(listModel);
		combo_dosen.setItemRenderer(new DosenRenderer());
		combo_dosen.getItemRenderer();
		
		
		System.out.println("render selesai");
	}
		
		class DosenRenderer implements ComboitemRenderer<Dosen>{

			@Override
			public void render(Comboitem item, Dosen data, int index)
					throws Exception {
				// TODO Auto-generated method stub
				item.setValue(data);
				item.setLabel(data.getNama());
				System.out.println("data : "+data.getNama());
				
			}
			
		}


	
}
