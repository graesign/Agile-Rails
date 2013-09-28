package gui;


public class RefreshGui extends Thread{
	Paneel paneel;
	public RefreshGui(Paneel paneel){
		this.paneel = paneel;
	}

	/**
	 * deze functie voert elk aantal millisec een berekining uit voor alle railcabs
	 */
	public void run(){
		while(true){
		paneel.repaint();
		slaap(40);
		}
	}

	/**
	 * Laat de thread voor een aantal milliseconden slapen
	 * @param millis
	 */
	public void slaap(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
}