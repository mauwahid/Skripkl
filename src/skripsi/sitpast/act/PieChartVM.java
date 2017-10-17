package skripsi.sitpast.act;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.PieModel;

public class PieChartVM {

	PieChartEngine engine;
	PieModel model;
	boolean threeD = true;
	String message;
	
	@Init
	public void init() {
		// prepare chart data
		engine = new PieChartEngine();

		model = ChartData.getModel();
	}

	public PieChartEngine getEngine() {
		return engine;
	}

	public PieModel getModel() {
		return model;
	}

	public boolean isThreeD() {
		return threeD;
	}
	
	public String getMessage(){
		return message;
	}
	
	@Command("showMessage") 
	@NotifyChange("message")
	public void onShowMessage(
			@BindingParam("msg") String message){
		this.message = message;
	}
	
	@GlobalCommand("dataChanged") 
	@NotifyChange("model")
	public void onDataChanged(
			@BindingParam("category")String category,
			@BindingParam("num") Number num){
		model.setValue(category, num);
	}
	
	@GlobalCommand("configChanged") 
	@NotifyChange({"threeD","engine"})
	public void onConfigChanged(
			@BindingParam("threeD") boolean threeD,
			@BindingParam("exploded") boolean exploded){
		this.threeD = threeD;
		engine.setExplode(exploded);
	}
}
