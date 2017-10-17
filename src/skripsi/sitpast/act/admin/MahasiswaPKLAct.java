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

import skripsi.sitpast.domain.BimbinganPKL;
import skripsi.sitpast.domain.Dosen;
import skripsi.sitpast.domain.PKL;

public class MahasiswaPKLAct extends GenericForwardComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7310524839071825751L;

	Dosen dosen;
	Window window;
	
	
	private SimpleDateFormat dateFormat;
	private Session sess;
	private List<PKL> listPKL;
	private SimpleListModel<PKL> model;
	private Listbox list_mahasiswa_pkl;
	
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
		//dosen = (Dosen) session.getAttribute("dosen");
		loadData("");
	}
	
	private void loadData(String query){
		

		String dataQuery = "%"+query+"%";
		sess = HibernateUtil.getSessionFactory().openSession();
		listPKL = new ArrayList<PKL>();
		
		Criteria criteria = sess.createCriteria(PKL.class);
	//	Criterion rest1 = Restrictions.eq("dospemPKL", dosen);
		
	//	criteria.add(rest1);
		criteria.addOrder(Order.asc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listPKL = criteria.list();
		model = new SimpleListModel<PKL>(listPKL);
		list_mahasiswa_pkl.setModel(model);
		list_mahasiswa_pkl.setItemRenderer(new PKLRenderer());
		list_mahasiswa_pkl.renderAll();	
		
		sess.close();
		
	}
	
	
	class PKLRenderer implements ListitemRenderer<PKL>{
		
		int index = 0;
		String status = "Belum diterima/Diajukan";

		@Override
		public void render(Listitem item, PKL data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			final PKL pklTemp = data;
			index = index + 1;
			new Listcell(""+index).setParent(item);
			new Listcell(data.getMahasiswa().getNim()).setParent(item);
			new Listcell(data.getMahasiswa().getNama()).setParent(item);
			new Listcell(data.getJudul()).setParent(item);
			new Listcell(data.getTanggalMulai()==null?"":dateFormat.format(data.getTanggalMulai())).setParent(item);
			new Listcell(data.getTanggalAkhir()==null?"":dateFormat.format(data.getTanggalAkhir())).setParent(item);
			new Listcell(data.getTempatPKL()==null?"":data.getTempatPKL()).setParent(item);
			new Listcell(data.getMahasiswa().getProdi().getNamaProdi()).setParent(item);
			
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
					onDetail(pklTemp);
					
				}
			});
			
			btnDetail.setParent(box);
			
			
			Listcell cellData = new Listcell();
			Button btnData = new Button("Data");
			btnData.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					onData(pklTemp);
					
				}
			});
			
			btnData.setParent(box);
			listcell.setParent(item);
			box.setParent(listcell);
		}
		
	}
	
	
	private void onDetail(PKL pkl){	
				//element
				window = new Window();
				window.setContentStyle("overflow:auto");
				
				window.setParent(self);
				window.setTitle("Detail PKL "+pkl.getMahasiswa().getNama());
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
				lbl_nim = new Label(pkl.getMahasiswa().getNim());
				lbl_nim.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("NIM"));
				lbl_nama = new Label(pkl.getMahasiswa().getNama());
				lbl_nama.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("NIM"));
				lbl_jurusan = new Label(pkl.getMahasiswa().getProdi().getNamaProdi());
				lbl_jurusan.setParent(row);
				
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Judul"));
				lbl_judul = new Label(pkl.getJudul());
				lbl_judul.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Mulai"));
				tgl_mulai = new Label(pkl.getTanggalMulai()==null?"":dateFormat.format(pkl.getTanggalMulai()));
				tgl_mulai.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Tanggal Akhir"));
				tgl_Seminar_prop = new Label(pkl.getTanggalAkhir()==null?"":dateFormat.format(pkl.getTanggalAkhir()));
				tgl_Seminar_prop.setParent(row);
				
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("No HP"));
				lbl_nohp = new Label(pkl.getMahasiswa().getNoTelp());
				lbl_nohp.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Email"));
				lbl_email = new Label(pkl.getMahasiswa().getEmail());
				lbl_email.setParent(row);
				
				row = new Row();
				row.setParent(rows);
				row.appendChild(new Label("Alamat"));
				lbl_alamat = new Label(pkl.getMahasiswa().getAlamat());
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
		

	private void onData(PKL pkl){	
		//element
		final PKL dataPKL = pkl;
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle("Data PKL "+dataPKL.getMahasiswa().getNama());
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
		lbl_dataAwal = new Label(dataPKL.getDataAwal());
		lbl_dataAwal.setParent(row);
		
		Button btnAwal = new Button("download");
		btnAwal.setParent(row);
		btnAwal.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDataAwal());
				Filedownload.save(file,null);
				
			}
		
		
		});
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 1"));
		lbl_bab1 = new Label(dataPKL.getDataBab1());
		lbl_bab1.setParent(row);
		
		btnDwnBab1 = new Button("download");
		btnDwnBab1.setParent(row);
		btnDwnBab1.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDataBab1());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 2"));
		lbl_bab2 = new Label(dataPKL.getDataBab2());
		lbl_bab2.setParent(row);
		
		btnDwnBab2 = new Button("download");
		btnDwnBab2.setParent(row);
		btnDwnBab2.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDataBab2());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label(dataPKL.getDataBab3()));
		lbl_bab3 = new Label("Backup.jpg");
		lbl_bab3.setParent(row);
		
		btnDwnBab3 = new Button("download");
		btnDwnBab3.setParent(row);
		btnDwnBab3.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDataBab3());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 4"));
		lbl_bab4 = new Label(dataPKL.getDataBab4());
		lbl_bab4.setParent(row);
		
		btnDwnBab4 = new Button("download");
		btnDwnBab4.setParent(row);
		btnDwnBab4.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDataBab4());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("BAB 5"));
		lbl_bab5 = new Label(dataPKL.getDataBab5());
		lbl_bab5.setParent(row);
		
		btnDwnBab5 = new Button("download");
		btnDwnBab5.setParent(row);
		btnDwnBab5.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDataBab5());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Daftar Pustaka"));
		lbl_dapus = new Label(dataPKL.getDaftarPustaka());
		lbl_dapus.setParent(row);
		
		btnDwnDapus = new Button("download");
		btnDwnDapus.setParent(row);
		btnDwnDapus.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getDaftarPustaka());
				Filedownload.save(file,null);
				
			}
		
		
		});	
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Lampiran"));
		lbl_lampiran = new Label(dataPKL.getLampiran());
		lbl_lampiran.setParent(row);
		
		btnDwnLamp = new Button("download");
		btnDwnLamp.setParent(row);
		btnDwnLamp.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				file = new File(path+"\\"+dataPKL.getLampiran());
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
