package UserInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import Business.Central;
import Business.StressTest;
import Business.Station;

public class StressTestPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JComboBox jComboBox1;
	private JLabel jLabel2;
	private JLabel Bestemming;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JButton jButton2;
	private JTextField jTextField1;
	private JButton jButton1;
	private JComboBox jComboBox6;
	private JComboBox jComboBox5;
	private JComboBox jComboBox4;
	private JComboBox jComboBox3;
	private JComboBox jComboBox2;
	private JLabel jLabel1;
	private int counter = 1;
	private String[] stations;
	private String[] hours;
	private String[] minutes;
	private int[] massiveReservation;
	private ArrayList<Station> stationsarray;
	public StressTest stresstest;
	private JOptionPane popup;

	/**
	 * Initialiseerd variabelen.
	 * 
	 * @param ArrayList -
	 *            stations
	 * @param Central -
	 *            central
	 */
	public StressTestPanel(ArrayList<Station> stations, Central central) {

		popup = new JOptionPane();

		stresstest = new StressTest(central);
		this.stationsarray = stations;
		this.stations = new String[stationsarray.size()];
		makeStationsArray();
		hours = new String[24];
		minutes = new String[60];

		fillTimeArrays();
		initGUI();
	}

	/**
	 * Kopieerd de stationArray van Central voor eigen gebruik, behoord bij de
	 * initialisatie.
	 */
	public void makeStationsArray() {
		for (int i = 0; i < stationsarray.size(); i++) {
			stations[i] = ((Station) stationsarray.get(i)).getStationName();
		}
	}

	/**
	 * Layout van panel word geinilialiseerd. Labels met informatie worden
	 * getekent. Eerste rij om in te vullen word getekent (drawCombo()).
	 */
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(702, 397));
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 7, 7 };
			this.setLayout(thisLayout);
			{
				jLabel1 = new JLabel("Station");
				this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setName("jLabel1");
			}
			{
				jLabel2 = new JLabel("Aantal passagiers");
				this.add(jLabel2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel2.setName("jLabel2");
			}
			{
				Bestemming = new JLabel("Bestemming");
				this.add(Bestemming, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				Bestemming.setName("Bestemming");
			}
			{
				jLabel3 = new JLabel("Tijd van");
				this.add(jLabel3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel3.setName("jLabel3");
			}
			{
				jLabel4 = new JLabel("Tijd tot");
				this.add(jLabel4, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel4.setName("jLabel4");
			}
			{
				jButton2 = new JButton("Start Test");
				this.add(jButton2, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton2.addActionListener(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		drawCombo();
	}

	/**
	 * Tekent bij aanroep een volgende rij om in te vullen voor de stresstest.
	 */
	private void drawCombo() {
		if (counter != 11) {
			{
				ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(stations);
				jComboBox1 = new JComboBox();
				this.add(jComboBox1, new GridBagConstraints(0, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBox1.setModel(jComboBox1Model);
			}
			{
				ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(stations);
				jComboBox2 = new JComboBox();
				this.add(jComboBox2, new GridBagConstraints(2, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBox2.setModel(jComboBox2Model);
			}
			{
				ComboBoxModel jComboBox3Model = new DefaultComboBoxModel(hours);
				jComboBox3 = new JComboBox();
				this.add(jComboBox3, new GridBagConstraints(3, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBox3.setModel(jComboBox3Model);
			}
			{
				ComboBoxModel jComboBox4Model = new DefaultComboBoxModel(minutes);
				jComboBox4 = new JComboBox();
				this.add(jComboBox4, new GridBagConstraints(4, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBox4.setModel(jComboBox4Model);
			}
			{
				ComboBoxModel jComboBox5Model = new DefaultComboBoxModel(hours);
				jComboBox5 = new JComboBox();
				this.add(jComboBox5, new GridBagConstraints(5, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBox5.setModel(jComboBox5Model);
			}
			{
				ComboBoxModel jComboBox6Model = new DefaultComboBoxModel(minutes);
				jComboBox6 = new JComboBox();
				this.add(jComboBox6, new GridBagConstraints(6, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBox6.setModel(jComboBox6Model);
			}
			{
				jButton1 = new JButton("Bevestig");
				this.add(jButton1, new GridBagConstraints(7, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton1.addActionListener(this);
			}
			{
				jTextField1 = new JFormattedTextField();
				this.add(jTextField1, new GridBagConstraints(1, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jTextField1.setPreferredSize(new java.awt.Dimension(75, 20));

			}
			counter++;
		}
	}

	/**
	 * zorgt dat alle acties worden afgehandeld.
	 * 
	 * @param AcationEvent
	 *            evt
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == jButton2) {
			stresstest.startStressTest();
			reset();
		}
		if (evt.getSource() == jButton1) {
			if (jComboBox1.getSelectedItem() == jComboBox2.getSelectedItem()) {
				popup.showMessageDialog(this, "De bestemming en het vertrek station zijn hetzelfde", "Fout",
						JOptionPane.ERROR_MESSAGE);

			} else {
				if (jComboBox3.getSelectedItem() == jComboBox5.getSelectedItem()
						&& jComboBox4.getSelectedItem() == jComboBox6.getSelectedItem()) {
					popup.showMessageDialog(this, "De vertektijd en de aankomsttijd zijn hetzelfde", "Fout",
							JOptionPane.ERROR_MESSAGE);

				} else {
					if (jTextField1.getText().isEmpty()) {
						popup.showMessageDialog(this, "Aantal passagiers veld is nog niet ingevuld", "Fout",
								JOptionPane.ERROR_MESSAGE);
					} else {
						if (Integer.parseInt(jTextField1.getText()) > 1000) {
							popup.showMessageDialog(this,
									"Het ingevoerde aantal passagiers mag niet meer dan 1000 bedragen", "Fout",
									JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								Integer.parseInt(jTextField1.getText());
								setMassiveReservationArray();
								stresstest.addMassiveReservation(massiveReservation);
								remove(jButton1);
								jComboBox1.setEnabled(false);
								jTextField1.setEnabled(false);
								jComboBox2.setEnabled(false);
								jComboBox3.setEnabled(false);
								jComboBox4.setEnabled(false);
								jComboBox5.setEnabled(false);
								jComboBox6.setEnabled(false);
								drawCombo();
							} catch (NumberFormatException nfe) {
								System.out.println("De passagier invoer is geen getal");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @return Station
	 */
	public String[] getStations() {
		return stations;
	}

	/**
	 * Vult de tijd arrays met uren en minuten. Behoord tot de initialisatie.
	 */
	public void fillTimeArrays() {
		for (int i = 0; i < 60; i++) {
			minutes[i] = "" + i;
		}
		for (int q = 0; q < 24; q++) {
			hours[q] = "" + q;
		}

	}

	/**
	 * Vult "massiveReservation" array met alle informatie om voor meerdere
	 * reserveringen binnen een bepaalde tijd met hetzelfde vertrek en
	 * eindstation.
	 */
	public void setMassiveReservationArray() {

		massiveReservation = new int[7];
		int i = 0;
		massiveReservation[i] = jComboBox1.getSelectedIndex();
		i++;
		massiveReservation[i] = Integer.parseInt(jTextField1.getText());
		i++;
		massiveReservation[i] = jComboBox2.getSelectedIndex();
		i++;
		massiveReservation[i] = Integer.parseInt(hours[jComboBox3.getSelectedIndex()]);
		i++;
		massiveReservation[i] = Integer.parseInt(minutes[jComboBox4.getSelectedIndex()]);
		i++;
		massiveReservation[i] = Integer.parseInt(hours[jComboBox5.getSelectedIndex()]);
		i++;
		massiveReservation[i] = Integer.parseInt(minutes[jComboBox6.getSelectedIndex()]);
		for (int q = 0; q < 7; q++) {
			System.out.println("" + massiveReservation[q]);
		}
	}

	/**
	 * Reset alle variabelen na het starten van de stresstest.
	 */
	public void reset() {
		counter = 1;
		removeAll();
		setLayout(null);
		makeStationsArray();
		fillTimeArrays();
		initGUI();
		repaint();
	}
}
