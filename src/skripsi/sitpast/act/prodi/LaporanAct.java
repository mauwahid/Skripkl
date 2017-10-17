package skripsi.sitpast.act.prodi;

import java.util.HashMap;
import java.util.Map;


import net.sf.jasperreports.engine.JRDataSource;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zul.Listbox;

public class LaporanAct extends GenericForwardComposer{

	
	Listbox format;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	
	
	JRDataSource dataSource;
	
	
	void showReport() {
		//Preparing parameters
		Map parameters = new HashMap();
		parameters.put("paramDosen1", "Dr. Maulana Wahid A");
		parameters.put("paramDosen2", "Dr. Faris Izzi");
		parameters.put("paramNamaMhs", "Nama Mahasiswa");
		parameters.put("paramNIMMhs", "1080909");
		parameters.put("paramProdi", "Computer Science");
		parameters.put("paramSkripsi", "Developing new Framework");
		parameters.put("paramNamaPudek", "Dr. Ridlo Qoma");
		parameters.put("paramGelarPudek", "Prof. PhD");
		parameters.put("paramNIPPudek", "10809809809");
		
		/*report.setSrc("../data/jasperreport.jasper");
		report.setParameters(parameters);
		
		report.setType((String) format.getSelectedItem().getValue());*/
	}
}
