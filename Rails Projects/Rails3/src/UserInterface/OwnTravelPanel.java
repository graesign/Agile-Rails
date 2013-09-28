package UserInterface;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import Business.*;

public class OwnTravelPanel extends JPanel implements ActionListener {

	private JComboBox kiesVertrekStation;
	private JComboBox kiesAankomstStation;
	private JComboBox kiesUren;
	private JComboBox kiesMinuten;
	private JButton bigButton;
	private JButton backButton;
	private BorderLayout bLayout;
	private GridBagLayout gbLayout;
	private JPanel centerPanel;
	private JLabel labelVertrekStation;
	private JLabel labelAankomstStation;
	private JLabel labelVertrekTijd;
	private Central central;
	private String[] stations;
	private ArrayList<Station> stationsArrayList;
	private String[] hours;
	private String[] minutes;
	private OwnTravelPanel2 ownTravelPanel2;
	private int scherm = 1;
	private String buttonText;
	private Calendar vertrekTijd;
	private Date date;
	private int code;
	public JButton buttonOK;
	private JOptionPane popup;

	public OwnTravelPanel(Central central) {
		popup = new JOptionPane();
		stations = new String[9];
		this.central = central;
		initLayout();
		this.add(centerPanel, bLayout.CENTER);
		centerPanel.setLayout(gbLayout);

		fillTimeArrays();
		initComboValue();
		drawText();
		buttonText = "Reserveren";
		bigButton = new JButton();
		backButton = new JButton();
		drawBigButton();
		drawComboBoxes();

	}

	public void fillTimeArrays() {
		hours = new String[24];
		minutes = new String[60];
		for (int i = 0; i < 60; i++) {
			minutes[i] = "" + i;
		}
		for (int q = 0; q < 24; q++) {
			hours[q] = "" + q;
		}

	}

	public void initComboValue() {
		stationsArrayList = central.getStationsArrayList();
		for (int i = 0; i < 9; i++) {
			stations[i] = stationsArrayList.get(i).getStationName();
		}
	}

	public void createReservation() {
		int jaar = central.getSimDate().get(Calendar.YEAR) - 1900;
		int maand = central.getSimDate().get(Calendar.MONTH);
		int dag = central.getSimDate().get(Calendar.DATE);
		int hour = this.kiesUren.getSelectedIndex();
		int min = this.kiesMinuten.getSelectedIndex();
		this.date = new Date(jaar, maand, dag, hour, min);
		this.vertrekTijd = Calendar.getInstance();
		this.vertrekTijd.setTime(date);

		if (!(this.kiesVertrekStation.getSelectedItem() == this.kiesAankomstStation.getSelectedItem())) {
			if (!this.vertrekTijd.after(central.getSimDate())) {
				dag += 1;
				this.date = new Date(jaar, maand, dag, hour, min);
				this.vertrekTijd.setTime(date);
			}
			Station vertrek = central.getStation(this.kiesVertrekStation.getSelectedIndex());
			Station bestemming = central.getStation(this.kiesAankomstStation.getSelectedIndex());

			code = central.generateCode(vertrek);
			central.createReservation(vertrek, bestemming, this.vertrekTijd, code, true);
		}
	}

	public void initLayout() {
		centerPanel = new JPanel();
		bLayout = new BorderLayout();
		this.setLayout(bLayout);
		gbLayout = new GridBagLayout();

		gbLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
		gbLayout.rowHeights = new int[] { 7, 7, 7, 7 };
		gbLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
		gbLayout.columnWidths = new int[] { 7, 7, 7, 7 };

	}

	public void drawBigButton() {
		bigButton.setText(buttonText);
		bigButton.addActionListener(this);
		this.add(bigButton, bLayout.SOUTH);
	}

	public void drawBackButton() {
		backButton.setText("Reservering aanpassen");
		backButton.addActionListener(this);
		// backButton.setSize(60, 20);
		centerPanel.add(backButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void drawComboBoxes() {
		kiesVertrekStation = new JComboBox(stations);
		kiesAankomstStation = new JComboBox(stations);
		kiesUren = new JComboBox(hours);
		kiesMinuten = new JComboBox(minutes);

		centerPanel.add(kiesVertrekStation, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		centerPanel.add(kiesAankomstStation, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		centerPanel.add(kiesUren, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		centerPanel.add(kiesMinuten, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	}

	public void drawText() {
		labelVertrekStation = new JLabel("Kies een vertrek station");
		centerPanel.add(labelVertrekStation, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		labelAankomstStation = new JLabel("Kiess een aankomst station");
		centerPanel.add(labelAankomstStation, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		labelVertrekTijd = new JLabel("vertrektijd");
		centerPanel.add(labelVertrekTijd, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	}

	public int getCode() {
		return this.code;
	}

	public void uppScherm() {
		scherm += 1;
	}

	public void setButtonTekst(String t) {
		this.buttonText = t;
	}

	public void startOver() {
		ownTravelPanel2.setVisible(false);
		centerPanel.setVisible(true);
		buttonText = "Reserveren";
		bigButton.setText(buttonText);
		bigButton.setVisible(true);
		backButton.setVisible(false);
		kiesVertrekStation.setEnabled(true);
		this.kiesVertrekStation.setSelectedIndex(0);
		kiesAankomstStation.setEnabled(true);
		this.kiesAankomstStation.setSelectedIndex(0);
		this.kiesUren.setEnabled(true);
		this.kiesUren.setSelectedIndex(0);
		this.kiesMinuten.setEnabled(true);
		this.kiesMinuten.setSelectedIndex(0);
		scherm = 1;
	}

	public void actionPerformed(ActionEvent evt) {
		/**
		 * Wanneer de gegevens juist zijn kan de gebruiker op de grote knop
		 * drukken. hier word dan naar scherm 2 gegaan. ook word de grote knop
		 * uitgezet.en er word een extra ok knop bijgetekend.
		 */
		if (evt.getSource() == bigButton && (scherm == 2)) {
			this.createReservation();

			ownTravelPanel2 = new OwnTravelPanel2(this, central);
			this.add(ownTravelPanel2, bLayout.CENTER);

			centerPanel.setVisible(false);
			bigButton.setVisible(false);

		}

		/**
		 * Hier word afgehandeld wat er gebeurt wanneer de gebruiker op de
		 * terug-knop drukt: alle comboboxen worden weer bewerkbaar gemaakt en
		 * de grote knop text word weer op reserveren gezet.
		 */
		if ((evt.getSource() == backButton)) {
			this.kiesVertrekStation.setEnabled(true);
			this.kiesAankomstStation.setEnabled(true);
			this.kiesUren.setEnabled(true);
			this.kiesMinuten.setEnabled(true);
			backButton.setVisible(false);
			buttonText = "Reserveren";
			bigButton.setText(buttonText);
			scherm = 1;
		}

		/**
		 * hier word geregeld wat er moet gebeuren wanneer de grote knop voor de
		 * eerste keer word ingedrukt. alle combo boxen worden onbewerkbaar en
		 * er komt tekst en een terug-knop bij.
		 */
		if (evt.getSource() == bigButton) {
			if (!(this.kiesVertrekStation.getSelectedItem().equals(this.kiesAankomstStation.getSelectedItem()))) {
				this.kiesVertrekStation.setEnabled(false);
				this.kiesAankomstStation.setEnabled(false);
				this.kiesUren.setEnabled(false);
				this.kiesMinuten.setEnabled(false);
				buttonText = "Deze gegevens zijn juist, Reserveer";
				bigButton.setText(buttonText);
				drawBackButton();
				backButton.setVisible(true);
				scherm++;
			} else {
				popup.showMessageDialog(this, "De bestemming en het vertrek station zijn hetzelfde", "Fout",
						JOptionPane.ERROR_MESSAGE);
			}

		}

	}
}
