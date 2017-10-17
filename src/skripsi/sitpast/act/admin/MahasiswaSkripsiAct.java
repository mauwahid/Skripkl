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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.ItemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import skripsi.sitpast.domain.BimbinganSkripsi;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.Skripsi;

public class MahasiswaSkripsiAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7310524839071825751L;

	Dosen dosen;
	Window window;
	
	
	private SimpleDateFormat dateFormat;
	private Session sess;
	private List<Skripsi> listSkripsi;
	private SimpleListModel<Skripsi> model;
	private Listbox list_mahasiswa_skripsi;
	
	Label lbl_nim;
	Label lbl_nama;
	Label lbl_judul;
	Label lbl_jurusan;
	Label dosen_pembimbing1;
	Label dosen_pembimbing2;
	Label tgl_mulai;
	Label tgl_Seminar_prop;
	Label tgl_seminar_hasil;
	Label tgl_selesai;
	Label tgl_sidang;
	Label lbl_nohp;
	Label lbl_email;
	Label lbl_alamat;
	
	
	Label lbl_dataAwal;
	Label lbl_bab1;
	Label lbl_bab2;
	Label lbl_bab3;
	Label lbl_bab4;
	Label lbl_bab5;
	Label lbl_lampiran;
	Label lbl_dapus;
	
	Button btnDownload;
	Button btnDwnBab1;
	Button btnDwnBab2;
	Button btnDwnBab3;
	Button btnDwnBab4;
	Button btnDwnBab5;
	Button btnDwnLamp;
	Button btnDwnDapus;
	
	
	
	
	private File file;
	private String path = "D:\\";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	//	dosen = (Dosen) session.getAttribute("dosen");
		loadData("");
	}
	
	private void loadData(String query){
		

		String dataQuery = "%"+query+"%";
		sess = HibernateUtil.getSessionFactory().openSession();
		listSkripsi = new ArrayList<Skripsi>();
		
		Criteria criteria = sess.createCriteria(Skripsi.class);
	//	Criterion rest1 = Restrictions.eq("dosPemSatu", dosen);
	//	Criterion rest2 = Restrictions.eq("dosPemDua",dosen);
		
	//	criteria.add(Restrictions.or(rest1,rest2));
	//	criteria.add(rest1);
		criteria.addOrder(Order.asc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listSkripsi = criteria.list();
		model = new SimpleListModel<Skripsi>(listSkripsi);
		list_mahasiswa_skripsi.setModel(model);
		list_mahasiswa_skripsi.setItemRenderer(new SkripsiRenderer());
		list_mahasiswa_skripsi.renderAll();	
		
		sess.close();
		
	}
	
	
	class SkripsiRenderer implements ListitemRenderer<Skripsi>{
		
		int index = 0;
		String status = "Belum diterima/Diajukan";

		@Override
		public void render(Listitem item, Skripsi data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			final Skripsi skripsiTemp = data;
			index = index + 1;
			new Listcell(""+index).setParent(item);
			new Listcell(data.getMahasiswa().getNim()).setParent(item);
			new Listcell(data.getMahasiswa().getNama()).setParent(item);
			new Listcell(data.getJudulSkripsi()).setParent(item);
			new Listcell(data.getTanggalMulai()==null?"":dateFormat.format(data.getTanggalMulai())).setParent(item);
			new Listcell(data.getTanggalSeminarProp()==null?"":dateFormat.format(data.getTanggalSeminarProp())).setParent(item);
			new Listcell(data.getTanggalSeminar()==null?"":dateFormat.format(data.getTanggalSeminar())).setParent(item);
			new Listcell(data.getTanggalSelesai()==null?"":dateFormat.format(data.getTanggalSelesai())).setParent(item);
			new Listcell(data.getTanggalSidang()==null?"":dateFormat.format(data.getTanggalSidang())).setParent(item);
			new Listcell(data.getMahasiswa().getProdi().getNamaProdi()).setParent(item);
			
			new  Listcell(data.getDosPemDua()!=null?data.getDosPemDua().getNama():"Belum ditentukan").setParent(item);
			//0 diajukan/belum disetujui, 2 disetujui/aktiv, 3 lulus, 4.ditolak, 5.belum
			if(data.getStatus()==1)
				status = "Aktif / Disetujui";
			else if(data.getStatus()==2)
				status = "Lulus";
			else if(data.getStatus()==3)
				status = "Ditolak";
			new Listcell(status).setParent(item);
			
			Hbox box = new Hbox();
			
			Listcell listcell = new Listcell();
			Button btnDetail = new Button("Detail");
			btnDetail.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onDetail(skripsiTemp);
					
				}
			});
			
			btnDetail.setParent(box);
			
			
			Listcell cellData = new Listcell();
			Button btnData = new Button("Data");
			btnData.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onData(skripsiTemp);
					
				}
			});
			
			btnData.setParent(box);
			listcell.setParent(item);
			box.setParent(listcell);
		}
		
	}
	
	
	private void onDetail(Skripsi skripsi){	
				//element
				window = new Window();
				window.setContentStyle("overflow:auto");
				
				window.setParent(self);
				window.setTitle("Detail Skripsi "+skripsi.getMahasiswa().getNama());
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
				row.appendChild(new Label("NIM"));
				lbl_nim = new Label(skripsi.getMahasiswa().getNim());
				lbl_nim.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("NIM"));
				lbl_nama = new Label(skripsi.getMahasiswa().getNama());
				lbl_nama.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("NIM"));
				lbl_jurusan = new Label(skripsi.getMahasiswa().getProdi().getNamaProdi());
				lbl_jurusan.setParent(row);
				
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Judul"));
				lbl_judul = new Label(skripsi.getJudulSkripsi());
				lbl_judul.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Mulai"));
				tgl_mulai = new Label(skripsi.getTanggalMulai()==null?"":dateFormat.format(skripsi.getTanggalMulai()));
				tgl_mulai.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Seminar Proposal"));
				tgl_Seminar_prop = new Label(skripsi.getTanggalSeminarProp()==null?"":dateFormat.format(skripsi.getTanggalSeminarProp()));
				tgl_Seminar_prop.setParent(row);
				
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Seminar Hasil"));
				tgl_seminar_hasil = new Label(skripsi.getTanggalSeminar()==null?"":dateFormat.format(skripsi.getTanggalSeminar()));
				tgl_seminar_hasil.setParent(row);
				
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Selesai"));
				tgl_selesai = new Label(skripsi.getTanggalSeminar()==null?"":dateFormat.format(skripsi.getTanggalSelesai()));
				tgl_selesai.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Sidang"));
				tgl_sidang = new Label(skripsi.getTanggalSidang()==null?"":dateFormat.format(skripsi.getTanggalSidang()));
				tgl_sidang.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("No HP"));
				lbl_nohp = new Label(skripsi.getMahasiswa().getNoTelp());
				lbl_nohp.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Email"));
				lbl_email = new Label(skripsi.getMahasiswa().getEmail());
				lbl_email.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Alamat"));
				lbl_alamat = new Label(skripsi.getMahasiswa().getAlamat());
				lbl_alamat.setParent(row);
				
				
				
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label(""));
				Button btn_batal = new Button("Tutup");
				btn_batal.setParent(row);
				
				window.onModal();
				
				btn_batal.addEventListener("onClick", new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						
						window.onClose();
					}
				
				});
				
						
			}
		

	private void onData(Skripsi skripsi){	
		//element
		final Skripsi dataSkripsi = skripsi;
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle("Data Skripsi "+dataSkripsi.getMahasiswa().getNama());
		window.setMode("popup");
		window.setPosition("center,top");
		window.setClosable(true);
		window.setWidth("700px");
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
		
		column = new Column();
		column.setParent(columns);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB Awal"));
		lbl_dataAwal = new Label(dataSkripsi.getDataAwal());
		lbl_dataAwal.setParent(row);
		
		Button btnAwal = new Button("download");
		btnAwal.setParent(row);
		btnAwal.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDataAwal());
				Filedownload.save(file,null);
				
			}
		
		
		});
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 1"));
		lbl_bab1 = new Label(dataSkripsi.getDataBab1());
		lbl_bab1.setParent(row);
		
		btnDwnBab1 = new Button("download");
		btnDwnBab1.setParent(row);
		btnDwnBab1.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDataBab1());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 2"));
		lbl_bab2 = new Label(dataSkripsi.getDataBab2());
		lbl_bab2.setParent(row);
		
		btnDwnBab2 = new Button("download");
		btnDwnBab2.setParent(row);
		btnDwnBab2.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDataBab2());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label(dataSkripsi.getDataBab3()));
		lbl_bab3 = new Label("Backup.jpg");
		lbl_bab3.setParent(row);
		
		btnDwnBab3 = new Button("download");
		btnDwnBab3.setParent(row);
		btnDwnBab3.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDataBab3());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 4"));
		lbl_bab4 = new Label(dataSkripsi.getDataBab4());
		lbl_bab4.setParent(row);
		
		btnDwnBab4 = new Button("download");
		btnDwnBab4.setParent(row);
		btnDwnBab4.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDataBab4());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 5"));
		lbl_bab5 = new Label(dataSkripsi.getDataBab5());
		lbl_bab5.setParent(row);
		
		btnDwnBab5 = new Button("download");
		btnDwnBab5.setParent(row);
		btnDwnBab5.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDataBab5());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Daftar Pustaka"));
		lbl_dapus = new Label(dataSkripsi.getDaftarPustaka());
		lbl_dapus.setParent(row);
		
		btnDwnDapus = new Button("download");
		btnDwnDapus.setParent(row);
		btnDwnDapus.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getDaftarPustaka());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Lampiran"));
		lbl_lampiran = new Label(dataSkripsi.getLampiran());
		lbl_lampiran.setParent(row);
		
		btnDwnLamp = new Button("download");
		btnDwnLamp.setParent(row);
		btnDwnLamp.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataSkripsi.getLampiran());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label(""));
		Button btn_batal = new Button("Tutup");
		btn_batal.setParent(row);
		
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
