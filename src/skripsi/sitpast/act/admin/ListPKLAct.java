package skripsi.sitpast.act.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
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
import org.zkoss.zul.Detail;
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

import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Prodi;
import skripsi.sitpast.domain.PKL;
import skripsi.sitpast.domain.UserTable;

public class ListPKLAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 277573288932410254L;
	List<PKL> listPKL;
	PKL pkl;
	Session sess;
	UserTable user;
	ListModel<PKL> model;
	ListModel<Prodi> modelProdi;
	ListModel<Dosen> modelDosen;
	List<Prodi> listProdi;
	List<Dosen> listDosen;
	Textbox txt_cari;
	
	//Component
	Listbox list_pkl;
	Window window;
	Label lbl_nama;
	Label lbl_nim;
	Combobox dospem_satu;
	Combobox dospem_dua;
	Datebox tgl_akhir;
	Textbox txt_noTelp;
	Textbox txt_email;
	Textbox txt_angkatan;
	Textbox txt_username;
	Textbox txt_password;
	Button btn_simpan;
	Button btn_batal;
	Combobox combo_prodi;
	Combobox combo_dosen;
	
	
	String txtBoxWidth = "200px";
	Detail detail;
	SimpleDateFormat dateFormat;
	
	//Component PKL
	Label lbl_judul;
	Textbox txt_tempat;
	Datebox tgl_mulai;
	Datebox tgl_selesai;
	Combobox combo_status;
	Label lbl_tempat;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		user = (UserTable) session.getAttribute("user");
		loadData("");
	}
	
	
	
	
	public void onDelete(PKL data){
		
		final PKL pkl = data;
		
		Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
			int i = new Integer(event.getData().toString());
			
				if(i == Messagebox.OK)
					deletePKL(pkl);
				
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
	
	
	private void onSave(PKL data){
		
		PKL pkl = new PKL();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			pkl.setId(data.getId());
		
		pkl.setJudul(lbl_judul.getValue());
		pkl.setTempatPKL(lbl_tempat.getValue());
		
		
		if(tgl_mulai.getValue()!=null)
			pkl.setTanggalMulai(tgl_mulai.getValue());
		if(tgl_selesai.getValue()!=null)
			pkl.setTanggalAkhir(tgl_selesai.getValue());
		
		pkl.setDospemPKL((Dosen)combo_dosen.getSelectedItem().getValue());
		
		if(combo_status.getSelectedIndex()==0)
			pkl.setStatus(1);
		if(combo_status.getSelectedIndex()==1)
			pkl.setStatus(2);
		if(combo_status.getSelectedIndex()==2)
			pkl.setStatus(3);
		if(combo_status.getSelectedIndex()==3)
			pkl.setStatus(0);
		
		
		sess.saveOrUpdate(pkl);
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

	private void showForm(PKL data){
		loadProdi();
		
		//data
		final PKL pkl;
		
		if(data!=null)
			pkl = data;
		else
			pkl = new PKL();
		
		//element
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle("Form PKL");
		window.setMode("popup");
		window.setPosition("center,top");
		window.setClosable(true);
		window.setWidth("700px");
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
		lbl_nama = new Label(pkl.getMahasiswa()==null?"":pkl.getMahasiswa().getNama());
		lbl_nama.setWidth(txtBoxWidth);
		lbl_nama.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("NIM"));
		lbl_nim = new Label(pkl.getMahasiswa()==null?"":pkl.getMahasiswa().getNim());
		lbl_nim.setWidth(txtBoxWidth);
		lbl_nim.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Judul"));
		lbl_judul = new Label(pkl.getJudul()==null?"":pkl.getJudul());
		lbl_judul.setParent(row);
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tempat"));
		lbl_tempat = new Label(pkl.getTempatPKL()==null?"":pkl.getTempatPKL());
		lbl_tempat.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Mulai"));
		tgl_mulai = new Datebox(pkl.getTanggalMulai()==null?null:pkl.getTanggalMulai());
	    tgl_mulai.setParent(row);
	    
	    row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Selesai"));
		tgl_selesai = new Datebox(pkl.getTanggalAkhir()==null?null:pkl.getTanggalAkhir());
	    tgl_selesai.setParent(row);
	    
	    row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing"));
		loadDosen();
		
		Comboitem itemDospem;
		combo_dosen.setParent(row);
		
		if(pkl.getDospemPKL()!=null){
			itemDospem = new Comboitem();
			itemDospem.setLabel(pkl.getDospemPKL().getNama());
			itemDospem.setValue(pkl.getDospemPKL());
			combo_dosen.appendChild(itemDospem);
			combo_dosen.setSelectedItem(itemDospem);
			System.out.println("Data dosen 1"+pkl.getDospemPKL().getNama());
	
		}
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Status"));
		combo_status = new Combobox();
		combo_status.setReadonly(true);
		Comboitem itemStatus1,itemStatus2,itemStatus3,itemStatus4;
		
		itemStatus1 = new Comboitem("Aktif/Distejui");
		itemStatus2 = new Comboitem("Lulus");
		itemStatus3 = new Comboitem("Ditolak");
		itemStatus4 = new Comboitem("Belum disetujui");
		
		combo_status.appendChild(itemStatus1);
		combo_status.appendChild(itemStatus2);
		combo_status.appendChild(itemStatus3);
		combo_status.appendChild(itemStatus4);
		
		if(pkl.getStatus()==1)
			combo_status.setSelectedIndex(0);
		else if(pkl.getStatus()==2)
			combo_status.setSelectedIndex(1);
		else if(pkl.getStatus()==3)
			combo_status.setSelectedIndex(2);
		else
			combo_status.setSelectedIndex(3);
		combo_status.setParent(row);
		
		
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
				onSave(pkl);
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
	
	
	public void onAdd(){
		showForm(null);
	}
	
	
	private void loadData(String query){
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		String dataQuery = "%"+query+"%";
		sess = HibernateUtil.getSessionFactory().openSession();
		listPKL = new ArrayList<PKL>();
		
		Criteria criteria = sess.createCriteria(PKL.class);
	//	listPKL = sess.createQuery("select distinct pkl from PKL as pkl, Mahasiswa as mahasiswa join pkl.mahasiswa " +
		//		"where mahasiswa.prodi.id = "+user.getProdi().getId()).list();
		Criterion rest1 = Restrictions.ilike("judul", dataQuery);
	//	Criterion rest2 = 
	//	Criterion rest2 = Restrictions.ilike("username", dataQuery);
	//	criteria.add(Restrictions.or(rest1,rest2));
	//	criteria.add(rest1);
	//	criteria.addOrder(Order.desc("id"));
	//	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listPKL = criteria.list();
		//listPKL = queryResult.list();
		model = new SimpleListModel<PKL>(listPKL);
		list_pkl.setModel(model);
		list_pkl.setItemRenderer(new PKLRenderer());
		list_pkl.renderAll();	
		
		sess.close();
		
	}
	
	class PKLRenderer implements ListitemRenderer<PKL>{

		int noUrut = 0;
		String tglMulai = "";
		String tglSelesai = "";
		String status = "Belum disetujui / Diajukan";
		
		@Override
		public void render(Listitem item, PKL data, int index)
				throws Exception {
			index = index + 1;
			
			final PKL pklTemp = data;
			// TODO Auto-generated method stub
			new Listcell(""+index).setParent(item);
			new Listcell(data.getMahasiswa().getNama()).setParent(item);
			new Listcell(data.getMahasiswa().getNim()).setParent(item);
			new Listcell(data.getJudul()).setParent(item);
			new Listcell(data.getTempatPKL()).setParent(item);
			
			if(data.getTanggalMulai()!=null)
				tglMulai = dateFormat.format(data.getTanggalMulai());
			if(data.getTanggalAkhir()!=null)
				tglSelesai = dateFormat.format(data.getTanggalAkhir());
			
			new Listcell(tglMulai).setParent(item);
			new Listcell(tglSelesai).setParent(item);
			
			new Listcell(data.getDospemPKL()!=null?data.getDospemPKL().getNama()+" , "+data.getDospemPKL().getGelar():"Belum ditentukan").setParent(item);
			//0 diajukan/belum disetujui, 2 disetujui/aktiv, 3 lulus, 4.ditolak, 5.belum
			if(data.getStatus()==1)
				status = "Aktif / Disetujui";
			else if(data.getStatus()==2)
				status = "Lulus";
			else if(data.getStatus()==3)
				status = "Ditolak";
			new Listcell(status).setParent(item);
			
			Hbox box = new Hbox();
			Button btnEdit = new Button("Ubah");
			btnEdit.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showForm(pklTemp);
				}
			
			
			});
			
			Button btnDelete = new Button("Hapus");
			btnDelete.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDelete(pklTemp);
					
				}
			});
		
			btnEdit.setParent(box);
			btnDelete.setParent(box);
			Listcell listcell = new Listcell();
			listcell.appendChild(box);
			listcell.setParent(item);
			
		}	
		
	}
	
	
	private void deletePKL(PKL pkl){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.delete(pkl);
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
	
	/*private void onPKL(PKL data){
		//data
		final PKL pkl;
		final PKL pkl;
		String dateStart;
		String dateEnd;
		
		if(data!=null)
			pkl = data;
		else
			pkl = new PKL();
		
		if(pkl.getPkl()==null)
		{
			pkl = new PKL();
			pkl.setPkl(pkl);
		}
			
	
		
		//element
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle("Form PKL");
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
		row.appendChild(new Label("Judul"));
		txt_judul = new Textbox(pkl.getPkl().getJudul()==null?"":pkl.getPkl().getJudul());
		txt_judul.setWidth(txtBoxWidth);
		txt_judul.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Lokasi"));
		txt_tempat = new Textbox(pkl.getPkl().getTempatPKL()==null?"":pkl.getPkl().getTempatPKL());
		txt_tempat.setWidth(txtBoxWidth);
		txt_tempat.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing I"));
		txt_username = new Textbox(pkl.getPkl().getJudul()==null?"":pkl.getPkl().getJudul());
		txt_username.setWidth(txtBoxWidth);
		txt_username.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing II"));
		txt_password = new Textbox(pkl.getPkl()==null?"":pkl.getPkl().getJudul());
		txt_password.setWidth(txtBoxWidth);
		txt_password.setParent(row);
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Mulai"));
		date_start = new Datebox();
		
		if(pkl.getPkl().getTanggalMulai()!=null)
		{
			dateStart = dateFormat.format(pkl.getPkl().getTanggalMulai());
			date_start.setValue(pkl.getPkl().getTanggalMulai());
		}				
		date_start.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Akhir"));
		date_end = new Datebox();
		if(pkl.getPkl().getTanggalAkhir()!=null)
		{
			dateStart = dateFormat.format(pkl.getPkl().getTanggalAkhir());
			date_end.setValue(pkl.getPkl().getTanggalAkhir());
		}				
		date_end.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Data PKL"));
		upload_pkl = new Button("Upload ZIP");
		upload_pkl.setParent(row);
		upload_pkl.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				File file = new File("C:\\"+pkl.getNama()+"-pkl.zip");
				Media media = Fileupload.get();
				Files.copy(file, media.getStreamData());
			}
		
		});
		
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
				onSave(pkl);
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
					
	}*/
	
	private void loadDosen(){
		combo_dosen = new Combobox();
		combo_dosen.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listDosen = new ArrayList<Dosen>();
		listDosen = sess.createCriteria(Dosen.class).list();
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
