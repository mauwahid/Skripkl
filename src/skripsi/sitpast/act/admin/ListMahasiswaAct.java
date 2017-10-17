package skripsi.sitpast.act.admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Fileupload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Box;
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

import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.PKL;
import skripsi.sitpast.domain.Prodi;
import skripsi.sitpast.domain.UserTable;

public class ListMahasiswaAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2564543135112186792L;
	// Data
		List<Mahasiswa> listMahasiswa;
		Mahasiswa mahasiswa;
		Session sess;
		ListModel<Mahasiswa> model;
		ListModel<Prodi> modelProdi;
		List<Prodi> listProdi;
		Textbox txt_cari;
		UserTable user;
		
		//Component
		Listbox list_mahasiswa;
		Window window;
		Textbox txt_nama;
		Textbox txt_nim;
		Textbox txt_alamat;
		Textbox txt_noTelp;
		Textbox txt_email;
		Textbox txt_angkatan;
		Textbox txt_username;
		Textbox txt_password;
		Button btn_simpan;
		Button btn_batal;
		Label lbl_prodi;
		Combobox combo_prodi;
		String txtBoxWidth = "200px";
		Detail detail;
		SimpleDateFormat dateFormat;
		
		//Component Skripsi
		Textbox txt_judul;
		Textbox txt_tempat;
		Datebox date_start;
		Datebox date_end;
		Button upload_pkl;
		Button upload_skripsi;
		
		@Override
		public void doAfterCompose(Component comp) throws Exception {
			// TODO Auto-generated method stub
			super.doAfterCompose(comp);
			user = (UserTable) session.getAttribute("user");
			loadData("");
		}
		
		public void onAdd(){
			showForm(null);
		}
		
		
		
		public void onDelete(Mahasiswa data){
			
			final Mahasiswa mahasiswa = data;
			
			Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
				
				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
				int i = new Integer(event.getData().toString());
				
					if(i == Messagebox.OK)
						deleteMahasiswa(mahasiswa);
					
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
		
		
		private void onSave(Mahasiswa data){
			
			Mahasiswa mahasiswa = new Mahasiswa();
			
			sess = HibernateUtil.getSessionFactory().openSession();
			sess.beginTransaction();
			
			if(data!=null)
				mahasiswa.setId(data.getId());
			
			mahasiswa.setNama(txt_nama.getText());
			mahasiswa.setAlamat(txt_alamat.getText());
			mahasiswa.setNim(txt_nim.getText());
			mahasiswa.setAngkatan(txt_angkatan.getText());
			mahasiswa.setNoTelp(txt_noTelp.getText());
			mahasiswa.setEmail(txt_email.getText());
			mahasiswa.setProdi(user.getProdi());
			mahasiswa.setPassword(txt_password.getText());
			mahasiswa.setUsername(txt_username.getText());
			
			
			sess.saveOrUpdate(mahasiswa);
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

		private void showForm(Mahasiswa data){
		//	loadProdi();
			
			//data
			final Mahasiswa mahasiswa;
			
			if(data!=null)
				mahasiswa = data;
			else
				mahasiswa = new Mahasiswa();
			
			//element
			window = new Window();
			window.setContentStyle("overflow:auto");
			
			window.setParent(self);
			window.setTitle("Form Mahasiswa");
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
			txt_nama = new Textbox(mahasiswa.getNama()==null?"":mahasiswa.getNama());
			txt_nama.setWidth(txtBoxWidth);
			txt_nama.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("NIM"));
			txt_nim = new Textbox(mahasiswa.getNim()==null?"":mahasiswa.getNim());
			txt_nim.setWidth(txtBoxWidth);
			txt_nim.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Username"));
			txt_username = new Textbox(mahasiswa.getUsername()==null?"":mahasiswa.getUsername());
			txt_username.setWidth(txtBoxWidth);
			txt_username.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Password"));
			txt_password = new Textbox(mahasiswa.getPassword()==null?"":mahasiswa.getPassword());
			txt_password.setType("password");
			txt_password.setWidth(txtBoxWidth);
			txt_password.setParent(row);
			
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Angkatan"));
			txt_angkatan = new Textbox(mahasiswa.getAngkatan()==null?"":mahasiswa.getAngkatan());
			txt_angkatan.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Prodi"));
		/*	Comboitem item;
			
			
			if(mahasiswa.getProdi()!=null){
				System.out.println("ada prodi");
				item = new Comboitem();
				item.setLabel(mahasiswa.getProdi().getNamaProdi());
				item.setValue(mahasiswa.getProdi());
				combo_prodi.appendChild(item);
				System.out.println("Data prodi"+mahasiswa.getProdi().getNamaProdi());
				
			}
		
			row.appendChild(combo_prodi);
		*/	
			
			lbl_prodi = new Label(user.getProdi().getNamaProdi());
			lbl_prodi.setParent(row);
			
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Alamat"));
			txt_alamat = new Textbox(mahasiswa.getAlamat()==null?"":mahasiswa.getAlamat());
			txt_alamat.setWidth(txtBoxWidth);
			txt_alamat.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("No Telp"));
			txt_noTelp = new Textbox(mahasiswa.getNoTelp()==null?"":mahasiswa.getNoTelp());
			txt_noTelp.setWidth(txtBoxWidth);
			txt_noTelp.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Email"));
			txt_email = new Textbox(mahasiswa.getEmail()==null?"":mahasiswa.getEmail());
			txt_email.setWidth(txtBoxWidth);
			txt_email.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("PKL"));
			Button btn_pkl = new Button("Lihat Data");
			btn_pkl.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Skripsi"));
			Button btn_skripsi = new Button("Lihat Data");
			btn_skripsi.setParent(row);
			
			
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
					onSave(mahasiswa);
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
			
			btn_pkl.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onPKL(mahasiswa);
				}
			
			});
			
			btn_skripsi.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					//onSkripsi(mahasiswa);
				}
			
			});
			
		}
		
		
		
		private void loadData(String query){
			
			String dataQuery = "%"+query+"%";
			sess = HibernateUtil.getSessionFactory().openSession();
			listMahasiswa = new ArrayList<Mahasiswa>();
			
			/*Criteria criteria = sess.createCriteria(Mahasiswa.class);
			Criterion rest1 = Restrictions.ilike("nama", dataQuery);
			Criterion rest2 = Restrictions.ilike("username", dataQuery);
			criteria.add(Restrictions.or(rest1,rest2));
			criteria.addOrder(Order.desc("id"));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);*/
			
			listMahasiswa = sess.createQuery("select distinct mahasiswa from Mahasiswa as mahasiswa" +
							" where mahasiswa.prodi.id = "+user.getProdi().getId()).list();
				
			
		//	listMahasiswa = criteria.list();
			model = new SimpleListModel<Mahasiswa>(listMahasiswa);
			list_mahasiswa.setModel(model);
			list_mahasiswa.setItemRenderer(new MahasiswaRenderer());
			list_mahasiswa.renderAll();	
			
			sess.close();
			
		}
		
		class MahasiswaRenderer implements ListitemRenderer<Mahasiswa>{

			int noUrut = 0;
			
			@Override
			public void render(Listitem item, Mahasiswa data, int index)
					throws Exception {
				index = index + 1;
				
				final Mahasiswa mahasiswaTemp = data;
				// TODO Auto-generated method stub
				new Listcell(""+index).setParent(item);
				new Listcell(data.getNama()).setParent(item);
				new Listcell(data.getNim()).setParent(item);
				new Listcell(data.getAngkatan()).setParent(item);
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
						showForm(mahasiswaTemp);
					}
				
				
				});
				
				Button btnDelete = new Button("Hapus");
				btnDelete.addEventListener("onClick", new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						onDelete(mahasiswaTemp);
						
					}
				});
			
				btnEdit.setParent(box);
				btnDelete.setParent(box);
				Listcell listcell = new Listcell();
				listcell.appendChild(box);
				listcell.setParent(item);
				
			}	
			
		}
		
		
		private void deleteMahasiswa(Mahasiswa mahasiswa){
			sess = HibernateUtil.getSessionFactory().openSession();
			sess.beginTransaction();
			sess.delete(mahasiswa);
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
		
		private void onPKL(Mahasiswa data){
			//data
			final Mahasiswa mahasiswa;
			final PKL pkl;
			String dateStart;
			String dateEnd;
			
			if(data!=null)
				mahasiswa = data;
			else
				mahasiswa = new Mahasiswa();
			
			if(mahasiswa.getPkl()==null)
			{
				pkl = new PKL();
				mahasiswa.setPkl(pkl);
			}
				
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			//element
			window = new Window();
			window.setContentStyle("overflow:auto");
			
			window.setParent(self);
			window.setTitle("Form Skripsi");
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
			txt_judul = new Textbox(mahasiswa.getPkl().getJudul()==null?"":mahasiswa.getPkl().getJudul());
			txt_judul.setWidth(txtBoxWidth);
			txt_judul.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Lokasi"));
			txt_tempat = new Textbox(mahasiswa.getPkl().getTempatPKL()==null?"":mahasiswa.getPkl().getTempatPKL());
			txt_tempat.setWidth(txtBoxWidth);
			txt_tempat.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Dosen Pembimbing I"));
			txt_username = new Textbox(mahasiswa.getPkl().getJudul()==null?"":mahasiswa.getPkl().getJudul());
			txt_username.setWidth(txtBoxWidth);
			txt_username.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Dosen Pembimbing II"));
			txt_password = new Textbox(mahasiswa.getPkl()==null?"":mahasiswa.getPkl().getJudul());
			txt_password.setWidth(txtBoxWidth);
			txt_password.setParent(row);
			
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Tanggal Mulai"));
			date_start = new Datebox();
			
			if(mahasiswa.getPkl().getTanggalMulai()!=null)
			{
				dateStart = dateFormat.format(mahasiswa.getPkl().getTanggalMulai());
				date_start.setValue(mahasiswa.getPkl().getTanggalMulai());
			}				
			date_start.setParent(row);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Tanggal Akhir"));
			date_end = new Datebox();
			if(mahasiswa.getPkl().getTanggalAkhir()!=null)
			{
				dateStart = dateFormat.format(mahasiswa.getPkl().getTanggalAkhir());
				date_end.setValue(mahasiswa.getPkl().getTanggalAkhir());
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
					File file = new File("C:\\"+mahasiswa.getNama()+"-pkl.zip");
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
					onSave(mahasiswa);
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
		

}
