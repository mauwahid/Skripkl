package skripsi.sitpast.act.prodi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.hibernate.Criteria;
import org.hibernate.Query;
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
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Filedownload;
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
import skripsi.sitpast.domain.Skripsi;
import skripsi.sitpast.domain.PKL;
import skripsi.sitpast.domain.Prodi;
import skripsi.sitpast.domain.UserTable;

public class ListSkripsiAct extends GenericForwardComposer<Component>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -986012922946707470L;
	List<Skripsi> listSkripsi;
	Skripsi skripsi;
	Session sess;
	ListModel<Skripsi> model;
	ListModel<Prodi> modelProdi;
	ListModel<Dosen> modelDosen;
	List<Prodi> listProdi;
	List<Dosen> listDosen;
	Textbox txt_cari;
	
	//Component
	Listbox list_skripsi;
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
	Combobox combo_dosen1;
	Combobox combo_dosen2;
	String txtBoxWidth = "200px";
	Detail detail;
	SimpleDateFormat dateFormat;
	
	//Component Skripsi
	UserTable user;
	Label lbl_judul;
	Textbox txt_tempat;
	Datebox tgl_mulai;
	Datebox tgl_selesai;
	Datebox tgl_seminar;
	Datebox tgl_sidang;
	Datebox date_end;
	Button upload_pkl;
	Button upload_skripsi;
	Combobox combo_status;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		user = (UserTable) session.getAttribute("user");
		loadData("");
	}
	
	
	
	
	public void onDelete(Skripsi data){
		
		final Skripsi skripsi = data;
		
		Messagebox.show("Apakah Anda yakin ingin menghapus data ini?","Konfirmasi",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
			int i = new Integer(event.getData().toString());
			
				if(i == Messagebox.OK)
					deleteSkripsi(skripsi);
				
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
	
	
	private void onSave(Skripsi data){
		
		Skripsi skripsi = new Skripsi();
		
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		
		if(data!=null)
			skripsi.setId(data.getId());
		
		skripsi.setJudulSkripsi(lbl_judul.getValue());
		
		
		if(tgl_mulai.getValue()!=null)
			skripsi.setTanggalMulai(tgl_mulai.getValue());
		if(tgl_selesai.getValue()!=null)
			skripsi.setTanggalSelesai(tgl_selesai.getValue());
		if(tgl_sidang.getValue()!=null)
			skripsi.setTanggalSidang(tgl_sidang.getValue());
		if(tgl_seminar.getValue()!=null)
			skripsi.setTanggalSeminar(tgl_seminar.getValue());
		
		skripsi.setDosPemSatu((Dosen)combo_dosen1.getSelectedItem().getValue());
		skripsi.setDosPemDua((Dosen)combo_dosen2.getSelectedItem().getValue());
		
		if(combo_status.getSelectedIndex()==0)
			skripsi.setStatus(1);
		if(combo_status.getSelectedIndex()==1)
			skripsi.setStatus(2);
		if(combo_status.getSelectedIndex()==2)
			skripsi.setStatus(3);
		if(combo_status.getSelectedIndex()==3)
			skripsi.setStatus(0);
		
		
		sess.saveOrUpdate(skripsi);
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

	private void showForm(Skripsi data){
		loadProdi();
		
		//data
		final Skripsi skripsi;
		
		if(data!=null)
			skripsi = data;
		else
			skripsi = new Skripsi();
		
		//element
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle("Form Skripsi");
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
		lbl_nama = new Label(skripsi.getMahasiswa()==null?"":skripsi.getMahasiswa().getNama());
		lbl_nama.setWidth(txtBoxWidth);
		lbl_nama.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("NIM"));
		lbl_nim = new Label(skripsi.getMahasiswa()==null?"":skripsi.getMahasiswa().getNim());
		lbl_nim.setWidth(txtBoxWidth);
		lbl_nim.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Judul"));
		lbl_judul = new Label(skripsi.getJudulSkripsi()==null?"":skripsi.getJudulSkripsi());
		lbl_judul.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Mulai"));
		tgl_mulai = new Datebox(skripsi.getTanggalMulai()==null?null:skripsi.getTanggalMulai());
	    tgl_mulai.setParent(row);
	    
	    row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Selesai"));
		tgl_selesai = new Datebox(skripsi.getTanggalSelesai()==null?null:skripsi.getTanggalSelesai());
	    tgl_selesai.setParent(row);
	    
	    row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Seminar"));
		tgl_seminar = new Datebox(skripsi.getTanggalSeminar()==null?null:skripsi.getTanggalSeminar());
	    tgl_seminar.setParent(row);
	    
	    row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Sidang"));
	    tgl_sidang = new Datebox(skripsi.getTanggalSidang()==null?null:skripsi.getTanggalSidang());
	    tgl_sidang.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing I"));
		loadDosen1();
		
		Comboitem itemDospem1;
		combo_dosen1.setParent(row);
		
		if(skripsi.getDosPemSatu()!=null){
			itemDospem1 = new Comboitem();
			itemDospem1.setLabel(skripsi.getDosPemSatu().getNama());
			itemDospem1.setValue(skripsi.getDosPemSatu());
			combo_dosen1.appendChild(itemDospem1);
			combo_dosen1.setSelectedItem(itemDospem1);
			System.out.println("Data dosen 1"+skripsi.getDosPemSatu().getNama());
	
		}
		
		
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing II"));
		loadDosen2();
		
		Comboitem itemDospem2;
		
		if(skripsi.getDosPemDua()!=null){
			itemDospem2 = new Comboitem();
			itemDospem2.setLabel(skripsi.getDosPemDua().getNama());
			itemDospem2.setValue(skripsi.getDosPemDua());
			combo_dosen2.appendChild(itemDospem2);
			combo_dosen2.setSelectedItem(itemDospem2);
			System.out.println("Data dosen 1"+skripsi.getDosPemSatu().getNama());
	
		}
		
		combo_dosen2.setParent(row);
		
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
		
		if(skripsi.getStatus()==1)
			combo_status.setSelectedIndex(0);
		else if(skripsi.getStatus()==2)
			combo_status.setSelectedIndex(1);
		else if(skripsi.getStatus()==3)
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
				onSave(skripsi);
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
		listSkripsi = new ArrayList<Skripsi>();
		
		Criteria criteria = sess.createCriteria(Skripsi.class);
		listSkripsi = sess.createQuery("select distinct skripsi from Skripsi as skripsi, Mahasiswa as mahasiswa join skripsi.mahasiswa " +
		"where mahasiswa.prodi.id = "+user.getProdi().getId()).list();
		Criterion rest1 = Restrictions.ilike("judulSkripsi", dataQuery);
	//	Criterion rest2 = Restrictions.ilike("username", dataQuery);
	//	criteria.add(Restrictions.or(rest1,rest2));
		criteria.add(rest1);
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
	//	listSkripsi = criteria.list();
		model = new SimpleListModel<Skripsi>(listSkripsi);
		list_skripsi.setModel(model);
		list_skripsi.setItemRenderer(new SkripsiRenderer());
		list_skripsi.renderAll();	
		
		sess.close();
		
	}
	
	class SkripsiRenderer implements ListitemRenderer<Skripsi>{

		int noUrut = 0;
		String tglMulai = "";
		String tglSelesai = "";
		String status = "Belum disetujui / Diajukan";
		
		@Override
		public void render(Listitem item, Skripsi data, int index)
				throws Exception {
			index = index + 1;
			
			final Skripsi skripsiTemp = data;
			// TODO Auto-generated method stub
			new Listcell(""+index).setParent(item);
			new Listcell(skripsiTemp.getJudulSkripsi()).setParent(item);
			new Listcell(skripsiTemp.getMahasiswa().getNama()).setParent(item);
			new Listcell(skripsiTemp.getMahasiswa().getNim()).setParent(item);
			
			if(data.getTanggalMulai()!=null)
				tglMulai = dateFormat.format(skripsiTemp.getTanggalMulai());
			if(data.getTanggalSelesai()!=null)
				tglSelesai = dateFormat.format(skripsiTemp.getTanggalSelesai());
			
			
			new Listcell(tglMulai).setParent(item);
			new Listcell(tglSelesai).setParent(item);
			
			new Listcell(skripsiTemp.getDosPemSatu()!=null?skripsiTemp.getDosPemSatu().getNama():"Belum ditentukan").setParent(item);
			new  Listcell(skripsiTemp.getDosPemDua()!=null?skripsiTemp.getDosPemDua().getNama():"Belum ditentukan").setParent(item);
			//0 diajukan/belum disetujui, 2 disetujui/aktiv, 3 lulus, 4.ditolak, 5.belum
			if(skripsiTemp.getStatus()==1)
				status = "Aktif / Disetujui";
			else if(skripsiTemp.getStatus()==2)
				status = "Lulus";
			else if(skripsiTemp.getStatus()==3)
				status = "Ditolak";
			new Listcell(status).setParent(item);
			
			Hbox box = new Hbox();
			Button btnEdit = new Button("Ubah");
			btnEdit.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showForm(skripsiTemp);
				}
			
			
			});
			
			Button btnPrintSurat = new Button("Cetak Surat");
			btnPrintSurat.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					printLetter(skripsiTemp);
				}
				
			});
			
			Button btnDelete = new Button("Hapus");
			btnDelete.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDelete(skripsiTemp);
					
				}
			});
		
			btnEdit.setParent(box);
			btnDelete.setParent(box);
			btnPrintSurat.setParent(box);
			Listcell listcell = new Listcell();
			listcell.appendChild(box);
			listcell.setParent(item);
			
		}	
		
	}
	
	
	private void printLetter(Skripsi skripsi) throws JRException, FileNotFoundException{
		Map params = new HashMap();
	
			Jasperreport report = new Jasperreport();
           // report.setParent(box);
            report.setHeight("500px");
            Map parameters = new HashMap();
            
            parameters.put("paramDosen1",skripsi.getDosPemSatu().getNama());
            parameters.put("paramDosen2",skripsi.getDosPemDua().getNama());
            parameters.put("paramNamaMhs",skripsi.getMahasiswa().getNama());
            parameters.put("paramNimMhs", skripsi.getMahasiswa().getNim());
            parameters.put("paramProdi", skripsi.getMahasiswa().getProdi().getNamaProdi());
            parameters.put("paramSkripsi",skripsi.getJudulSkripsi());
            parameters.put("paramNamaPudek", "Agus Salim");
            parameters.put("paramGelarPudek", ", Dr, MSi");
            parameters.put("paramNamaPudek", "Dr Agus Salim, MSi");
            parameters.put("paramNIPPudek", "10809");
            
            System.out.println("Skripsi : "+skripsi.getDosPemDua().getNama());
    	//	parameters.put("id_mhs",aju_bea.getMahasiswa().getId());
    		
            Connection conn = HibernateUtil.getSessionFactory().openSession().connection();
            if(conn==null)
            	System.out.println("conn null");
            JasperPrint jasperPrint = null;
            if(parameters==null)
            	System.out.println("paramters null");
            
           
				jasperPrint = JasperFillManager.fillReport(application.getRealPath("/backend/data/SuratPemberitahuan.jasper"),
				        parameters, new JREmptyDataSource());
		
            File myFile = new File(application
                    .getRealPath("/backend/data/surat_keterangan.pdf"));
            myFile.getParentFile().mkdirs();
            JRAbstractExporter exporterPDF = new JRPdfExporter();
            exporterPDF.setParameter(JRXlsExporterParameter.JASPER_PRINT,
                    jasperPrint);
            
				exporterPDF.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
				        new FileOutputStream(myFile));
			
            exporterPDF.setParameter(JRExporterParameter.OUTPUT_FILE,
                    myFile);
            try {
				exporterPDF.exportReport();
			} catch (JRException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				Filedownload.save(myFile, "pdf");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             
	}
	
	private void deleteSkripsi(Skripsi skripsi){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.delete(skripsi);
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
	
	/*private void onPKL(Skripsi data){
		//data
		final Skripsi skripsi;
		final PKL pkl;
		String dateStart;
		String dateEnd;
		
		if(data!=null)
			skripsi = data;
		else
			skripsi = new Skripsi();
		
		if(skripsi.getPkl()==null)
		{
			pkl = new PKL();
			skripsi.setPkl(pkl);
		}
			
	
		
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
		txt_judul = new Textbox(skripsi.getPkl().getJudul()==null?"":skripsi.getPkl().getJudul());
		txt_judul.setWidth(txtBoxWidth);
		txt_judul.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Lokasi"));
		txt_tempat = new Textbox(skripsi.getPkl().getTempatPKL()==null?"":skripsi.getPkl().getTempatPKL());
		txt_tempat.setWidth(txtBoxWidth);
		txt_tempat.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing I"));
		txt_username = new Textbox(skripsi.getPkl().getJudul()==null?"":skripsi.getPkl().getJudul());
		txt_username.setWidth(txtBoxWidth);
		txt_username.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Dosen Pembimbing II"));
		txt_password = new Textbox(skripsi.getPkl()==null?"":skripsi.getPkl().getJudul());
		txt_password.setWidth(txtBoxWidth);
		txt_password.setParent(row);
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Mulai"));
		date_start = new Datebox();
		
		if(skripsi.getPkl().getTanggalMulai()!=null)
		{
			dateStart = dateFormat.format(skripsi.getPkl().getTanggalMulai());
			date_start.setValue(skripsi.getPkl().getTanggalMulai());
		}				
		date_start.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal Akhir"));
		date_end = new Datebox();
		if(skripsi.getPkl().getTanggalAkhir()!=null)
		{
			dateStart = dateFormat.format(skripsi.getPkl().getTanggalAkhir());
			date_end.setValue(skripsi.getPkl().getTanggalAkhir());
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
				File file = new File("C:\\"+skripsi.getNama()+"-pkl.zip");
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
				onSave(skripsi);
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
	
	private void loadDosen1(){
		combo_dosen1 = new Combobox();
		combo_dosen1.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listDosen = new ArrayList<Dosen>();
		listDosen = sess.createCriteria(Dosen.class).list();
		modelDosen = new SimpleListModel<Dosen>(listDosen);
		
		ListModelList<Dosen> listModel = new ListModelList<Dosen>(listDosen);
		listModel.addSelection(listModel.get(0));
		
		combo_dosen1.setModel(listModel);
		combo_dosen1.setItemRenderer(new DosenRenderer());
		combo_dosen1.getItemRenderer();
		
		
		System.out.println("render selesai");
	}
	
	private void loadDosen2(){
		combo_dosen2 = new Combobox();
		combo_dosen2.setReadonly(true);
		sess = HibernateUtil.getSessionFactory().openSession();
		listDosen = new ArrayList<Dosen>();
		listDosen = sess.createCriteria(Dosen.class).list();
		modelDosen = new SimpleListModel<Dosen>(listDosen);
		
		ListModelList<Dosen> listModel = new ListModelList<Dosen>(listDosen);
		listModel.addSelection(listModel.get(0));
		
		combo_dosen2.setModel(listModel);
		combo_dosen2.setItemRenderer(new DosenRenderer());
		combo_dosen2.getItemRenderer();
		
		
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
