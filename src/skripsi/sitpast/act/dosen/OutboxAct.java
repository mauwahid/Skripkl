package skripsi.sitpast.act.dosen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
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
import skripsi.sitpast.domain.Mahasiswa;
import skripsi.sitpast.domain.Pesan;

public class OutboxAct extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -639785028509174122L;
	Session sess;
	List<Pesan> pesans;
	Mahasiswa mahasiswa;
	Dosen dosen;
	ListModel<Pesan> modelPesan;
	DateFormat dateFormat;
	
	Listbox list_outbox;
	Window window;
	Window windowReply;
	
	Label lbl_tgl_pesan;
	Label lbl_isi_pesan;
	Label lbl_judul;
	Label lbl_ke;
	Button btnReply;
	Button btnClose;
	
	Textbox txt_judul;
	Textbox txt_isi;
	Button btnSend;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		dateFormat = new SimpleDateFormat("dd/mm/yyyy");
		dosen = (Dosen) session.getAttribute("dosen");
		loadPesan();
	}


	private void loadPesan(){
		sess = HibernateUtil.getSessionFactory().openSession();
		//String stringQuery = "from Pesan where toUser=:user";
		//query = sess.createQuery(stringQuery);
		//pesans = query.list();
		pesans = sess.createCriteria(Pesan.class).add(Restrictions.eq("fromDosen", dosen))
		.addOrder(Order.desc("waktu")).list();
		
		modelPesan = new SimpleListModel(pesans);
		list_outbox.setModel(modelPesan);
		list_outbox.setItemRenderer(new PesanRenderer());
		list_outbox.renderAll();
	}
	
	
	class PesanRenderer implements ListitemRenderer<Pesan>{

		int index = 0;
		
		@Override
		public void render(Listitem item, Pesan data, int index)
				throws Exception {
			// TODO Auto-generated method stub
			
			final Pesan pesan = data;
			index = index + 1;
			new Listcell(""+index).setParent(item);
			new Listcell(dateFormat.format(data.getWaktu())).setParent(item);
			if(data.getToMhs()!=null)
				new Listcell("Mahasiswa : "+data.getToMhs().getNama()).setParent(item);
			else
				new Listcell("Prodi : "+data.getToUser().getNamaLengkap()).setParent(item);
			new Listcell(data.getJudul()).setParent(item);
			new Listcell(data.getIsi()).setParent(item);
		
			if(data.getStatus()!=1){
				new Listcell("Belum dibaca").setParent(item);
			}
			
			else
				new Listcell("Telah dibaca").setParent(item);
			
			Listcell cell = new Listcell();
			Button btnBaca = new Button("Baca Pesan");
			btnBaca.addEventListener("onClick", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					// TODO Auto-generated method stub
					showPesan(pesan);
				}
			
			
			});
			
			btnBaca.setParent(cell);
			cell.setParent(item);
			
		}

		
	}
	
	
	private void showPesan(Pesan pesan){
		window = new Window();
		window.setContentStyle("overflow:auto");
		
		window.setParent(self);
		window.setTitle("Form Pesan");
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
		row.appendChild(new Label("Tujuan"));
		if(pesan.getToMhs()!=null){
			lbl_ke = new Label("Mahasiswa : "+pesan.getToMhs().getNama());
			lbl_ke.setParent(row);
		}else{
			lbl_ke = new Label("Prodi : "+pesan.getToUser().getNamaLengkap());
			lbl_ke.setParent(row);
		}
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tanggal"));
		lbl_tgl_pesan = new Label(dateFormat.format(pesan.getWaktu()));
		lbl_tgl_pesan.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Judul"));
		lbl_judul = new Label(pesan.getJudul());
		lbl_judul.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Isi Pesan"));
		lbl_isi_pesan = new Label(pesan.getIsi());
		lbl_isi_pesan.setWidth("300px");
		lbl_isi_pesan.setHeight("200px");
		lbl_isi_pesan.setMultiline(true);
		lbl_isi_pesan.setParent(row);
		
		
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Aksi"));
		Hlayout hlay = new Hlayout();
		hlay.appendChild(btnClose = new Button("Tutup"));
		row.appendChild(hlay);
		
		final Dosen toDosen = pesan.getFromDosen();
		
		window.onModal();
		
		btnClose.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				
				window.onClose();
			}
		
		});
		
				
	}
	
	
		
}
