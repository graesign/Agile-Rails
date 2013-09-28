import javax.swing.*;

import org.tigam.railcab.algoritme.ReisManager;

import org.tigam.railcab.algoritme.Simulatie;

import java.awt.*;

import java.awt.event.*;

import java.applet.*;

import java.io.BufferedReader;
import java.io.File;

import java.io.BufferedWriter;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.FileWriter;

import java.io.IOException;

import java.io.PrintStream;

import java.util.EventObject;

public class UserInterface extends JFrame {

	private JPanel simulatie;

	private Paneel paneel;

	private MenuItem momentOpslaan, momentLaden, baanInstelling, hulp,
			bestandSluiten, handleiding, about;

	private Controller controller;

	private ReisManager reisManager;

	private Simulatie simulatie_;

	private static boolean isGestart = false;

	private static boolean isGepauzeerd = false;

	private JFrame frame;

	public UserInterface() {

		paneel = new Paneel();

		simulatie = new Paneel();

		setContentPane(simulatie);

		controller = new Controller();

		frame = new JFrame();

		simulatie_ = Simulatie.getInstantie();

		reisManager = simulatie_.getReisManager();

		MenuBar menu = new MenuBar();

		setMenuBar(menu);

		/**
		 * maak de menu's aan
		 */

		Menu bestand = new Menu("Bestand");

		Menu opties = new Menu("Opties");

		Menu help = new Menu("Help");

		/**
		 * voeg items toe aan menu's
		 */

		momentOpslaan = new MenuItem("Momentopname Opslaan");

		baanInstelling = new MenuItem("Baan instellingen");

		bestandSluiten = new MenuItem("Afsluiten ALT + F4");

		handleiding = new MenuItem("Handleiding");

		about = new MenuItem("About");

		hulp = new MenuItem("Help");

		/**
		 * Hier worden de inwendigge klasses aangeroepen als er een aanroep naar
		 * die klasses zijn gedaan
		 */
		about.addActionListener(new About());

		handleiding.addActionListener(new Handleiding());

		baanInstelling.addActionListener(new Instelling());

		bestandSluiten.addActionListener(new Sluiten());

		momentOpslaan.addActionListener(new MomentOpnameOpslaan());

		/**
		 * voeg menu toe aan balk
		 */

		menu.add(bestand);

		menu.add(opties);

		menu.add(help);

		/**
		 * voeg menuitems toe aan 'bestand'
		 */

		bestand.add(momentOpslaan);

		bestand.add(bestandSluiten);

		/**
		 * voeg menuitems toe aan 'opties'
		 */

		opties.add(baanInstelling);

		/**
		 * voeg menuitems toe aan 'help'
		 */

		help.add(handleiding);

		help.add(about);

	}

	/**
	 * Deze methode houd de status bij van het simulatie. of het gestart is of
	 * niet gestart is d..m.v een true en false
	 */
	public static boolean isGestart(boolean test) {

		isGestart = test;

		return isGestart;

	}

	/**
	 * Deze methode houd de status bij van het simulatie. of het gepuazeerd is
	 * of niet gepauzeerd is d..m.v een true en false
	 */
	public static boolean isGepauzeerd(boolean test) {

		isGepauzeerd = test;

		return isGepauzeerd;

	}

	/**
	 * inwendige klasse voor het openen van de instellingen klasse in een frame.
	 */

	class Instelling implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			new InstellingScherm();

		}

	}

	/**
	 * inwendige klasse voor het sluiten van de simulatie.
	 */
	class Sluiten implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			dispose();

		}

	}

	/**
	 * inwendige klasse voor het opslaan van een momentopname Die heeft tevens
	 * een voorwaarde. Het mag alleen gedaan worden als de simulatie draait
	 */
	class MomentOpnameOpslaan implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (isGestart == true
					|| reisManager.getStatus() == ReisManager.START) {

				if (isGepauzeerd == false) {

					System.out.println("voert hij dit uit " + isGepauzeerd);

					controller.momentOpnameOpslaan();
					JOptionPane.showMessageDialog(frame, "Bestand opgeslagen");
				} else

				{

					JOptionPane.showMessageDialog(frame,
							"De Simulatie is gepauzeerd", "Fout",

							JOptionPane.ERROR_MESSAGE);

				}

			} else {

				JOptionPane.showMessageDialog(frame,
						"De Simulatie is nog niet gestart", "Fout",

						JOptionPane.ERROR_MESSAGE);

			}

		}

	}

	/**
	 * inwendige klasse voor het openen van de about frame
	 */
	class About implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new About();
		}
	}

	/**
	 * inwendige klasse voor openen van de handleiding pdf
	 */
	class Handleiding implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			boolean exists = (new File("src/handleiding.pdf")).exists();

			if (exists) {
				try {
					Runtime.getRuntime().exec(
							"rundll32 url.dll,FileProtocolHandler "
									+ "src/handleiding.pdf");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {

				JOptionPane.showMessageDialog(frame,
						"Handleiding niet gevonden", "Fout",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void main(String args[]) {

		JFrame frame = new UserInterface();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBounds(0, 0, 1024, 768);

		frame.setVisible(true);

	}

}
