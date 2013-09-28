package gui;

import javax.swing.JPanel;

import simulatie.*;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;
	Paneel paneel;
	Grafieken grafieken;

	/**
	 * In de constructor worden twee panelen aangemaakt een voor de grafieken en een voor het grafische plaatje.
	 * @param sim
	 */
	public GUI(Simulatie sim){
		paneel = new Paneel(sim);
		grafieken = new Grafieken(sim);

		add(paneel);
		add(grafieken);

		setLayout(null);
		paneel.setBounds(0, 0, 800, 800);
		grafieken.setBounds(800, 0, 400, 800);
	}
}
