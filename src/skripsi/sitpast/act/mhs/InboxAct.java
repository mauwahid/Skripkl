package skripsi.sitpast.act.mhs;

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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.ItemRenderer;
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



public class InboxAct extends GenericForwardComposer<Component>{

	
	Session sess;
	List<Pesan> pesans;
	Mahasiswa mahasiswa;
	ListModel<Pesan> modelPesan;
	DateFormat dateFormat;
	
	Listbox list_inbox;
	Window window;
	Window windowReply;
	
	Label lbl_tgl_pesan;
	Label lbl_isi_pesan;
	Label lbl_judul;
	Label lbl_dari;
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
		mahasiswa = (Mahasiswa) session.getAttribute("mahasiswa");
		loadPesan();
	}


	private void loadPesan(){
		sess = HibernateUtil.getSessionFactory().openSession();
		//String stringQuery = "from Pesan where toUser=:user";
		//query = sess.createQuery(stringQuery);
		//pesans = query.list();
		pesans = sess.createCriteria(Pesan.class).add(Restrictions.eq("toMhs", mahasiswa))
		.addOrder(Order.desc("waktu")).list();
		
		modelPesan = new SimpleListModel(pesans);
		list_inbox.setModel(modelPesan);
		list_inbox.setItemRenderer(new PesanRenderer());
		list_inbox.renderAll();
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
			new Listcell(data.getFromDosen().getNama()).setParent(item);
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
		row.appendChild(new Label("Pengirim"));
		lbl_dari = new Label(pesan.getFromDosen().getNama()+" , "+pesan.getFromDosen().getGelar());
		lbl_dari.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Aksi"));
		Hlayout hlay = new Hlayout();
		hlay.appendChild(btnReply = new Button("Balas"));
		hlay.appendChild(btnClose = new Button("Tutup"));
		row.appendChild(hlay);
		
		final Dosen toDosen = pesan.getFromDosen();
		
		btnReply.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				//onReply(toDosen);
			}
		
		});
		
		window.onModal();
		
		btnClose.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				
				window.onClose();
			}
		
		});
		
				
	}
	
	
	private void onRepy(Dosen dosen){
		window.onClose();
		
		windowReply = new Window();
		windowReply.setContentStyle("overflow:auto");
		
		windowReply.setParent(self);
		windowReply.setTitle("Kirim Pesan");
		windowReply.setMode("popup");
		windowReply.setPosition("center,top");
		windowReply.setClosable(true);
		windowReply.setWidth("400px");
		windowReply.setHeight("400px");
		windowReply.setVisible(true);
		
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
		txt_judul = new Textbox();
		txt_judul.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Isi Pesan"));
		txt_isi = new Textbox();
		txt_isi.setWidth("300px");
		txt_isi.setHeight("200px");
		txt_isi.setMultiline(true);
		txt_isi.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kepada"));
		lbl_dari = new Label(dosen.getNama()+" , "+dosen.getGelar());
		lbl_dari.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Aksi"));
		Hlayout hlay = new Hlayout();
		hlay.appendChild(btnReply = new Button("Balas"));
		hlay.appendChild(btnClose = new Button("Tutup"));
		row.appendChild(hlay);
		
		final Dosen toDosen = dosen;
		
		btnReply.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				onSend(toDosen);
			}
		
		});
		
		window.onModal();
		
		btnClose.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				
				windowReply.onClose();
			}
		
		});
		
	}
	
	
	private void onSend(Dosen dosen){
		sess = HibernateUtil.getSessionFactory().openSession();
		sess.beginTransaction();
		Pesan pesan = new Pesan();
		pesan.setIsi(txt_isi.getValue());
		pesan.setJudul(txt_judul.getValue());
		pesan.setToDosen(dosen);
		pesan.setWaktu(Calendar.getInstance().getTime());
		sess.save(pesan);
		
		Messagebox.show("Pesan Terkirim");
		windowReply.onClose();
	}
	
}
