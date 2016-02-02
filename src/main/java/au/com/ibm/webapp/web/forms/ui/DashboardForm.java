package au.com.ibm.webapp.web.forms.ui;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.PieChartModel;

@ManagedBean
public class DashboardForm {

	private PieChartModel pieModel1;
	private PieChartModel pieModel2;
	private PieChartModel pieModel3;

    @PostConstruct
    public void init() {
        createPieModels();
    }

    private void createPieModels() {
        createPieModel1();
        createPieModel2();
        createPieModel3();
    }

    private void createPieModel1() {
        pieModel1 = new PieChartModel();
        
        pieModel1.set("Brand 1", 540);
        pieModel1.set("Brand 2", 325);
        pieModel1.set("Brand 3", 702);
        pieModel1.set("Brand 4", 421);
        
        pieModel1.setExtender("skinPie");
        pieModel1.setShowDataLabels(true);
    }
    
    private void createPieModel2() {
        pieModel2 = new PieChartModel();
        
        pieModel2.set("Brand 1", 540);
        pieModel2.set("Brand 2", 325);
        pieModel2.set("Brand 3", 702);
        pieModel2.set("Brand 4", 421);
        
        pieModel2.setExtender("skinPie");
        pieModel2.setShowDataLabels(true);
    }

    private void createPieModel3() {
        pieModel3 = new PieChartModel();
        
        pieModel3.set("Brand 1", 540);
        pieModel3.set("Brand 2", 325);
        pieModel3.set("Brand 3", 702);
        pieModel3.set("Brand 4", 421);
        
        pieModel3.setExtender("skinPie");
        pieModel3.setShowDataLabels(true);
    }

	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}

	public PieChartModel getPieModel2() {
		return pieModel2;
	}

	public void setPieModel2(PieChartModel pieModel2) {
		this.pieModel2 = pieModel2;
	}

	public PieChartModel getPieModel3() {
		return pieModel3;
	}

	public void setPieModel3(PieChartModel pieModel3) {
		this.pieModel3 = pieModel3;
	}
    
    
}
