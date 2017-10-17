package skripsi.sitpast.act;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.zkoss.zkex.zul.impl.JFreeChartEngine;
import org.zkoss.zul.Chart;

/*
 * you are able to do many advanced chart customization by extending ChartEngine
 */
public class PieChartEngine extends JFreeChartEngine {
	
	private boolean explode = false;
	
	public boolean prepareJFreeChart(JFreeChart jfchart, Chart chart) {
		jfchart.setBorderPaint(Color.white);
		PiePlot piePlot = (PiePlot) jfchart.getPlot();
		piePlot.setLabelBackgroundPaint(Color.decode("#AFFEFF"));
		piePlot.setExplodePercent("Java", explode ? 0.2 : 0);
		return false;
	}

	public void setExplode(boolean explode) {
		this.explode = explode;
	}
}
