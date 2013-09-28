package gui;

public class UpdateGrafieken extends Thread {

	Grafieken grafieken;
	
	public UpdateGrafieken(Grafieken grafieken){
		this.grafieken = grafieken;
	}
	
	public void run(){
		while(true){
			grafieken.updateCharts();

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
