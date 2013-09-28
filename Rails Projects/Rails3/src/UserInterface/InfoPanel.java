package UserInterface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Business.*;
import Data.*;

public class InfoPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JSlider timeSlider;
	private JSlider aiSlider;
	private JButton stopAI;
	private JCheckBox startstopAI;
	private JCheckBox startstopPassengerAmount;
	private JCheckBox yellow;
	private JCheckBox black;
	private JCheckBox purple;
	private JCheckBox pink;
	private JCheckBox green;
	private Central central;
	private OverviewPanel overviewpanel;
	private JLabel acceleration;
	private JLabel aiLabel;
	private JButton drawButton;
	private JLabel bischShuttles;
	private JLabel aReisPass;
	private JLabel totRijzShuttles;
	private JLabel wPass;
	private JLabel startstopAIlabel;
	private JLabel startstopPassengerAmountlabel;
	private JLabel yellowLabel;
	private JLabel blackLabel;
	private JLabel purpleLabel;
	private JLabel pinkLabel;
	private JLabel greenLabel;
	private JFrame loginFrame;
	private OwnTravelPanel3 ownTravelPanel3;
	private int code;
	private JLabel slider;
	private ImageIcon bg = new ImageIcon("backgroundSlider.PNG");
	private JLabel bgAILabel = new JLabel(bg);
	private JLabel bgAccelerationLabel = new JLabel(bg);

	public InfoPanel(Central central, OverviewPanel overviewpanel) {

		this.central = central;
		initLayout();
		drawButton = new JButton();
		drawButton.setBounds(150, 80, 160, 25);
		drawButton.addActionListener(new incheckenPerron());

		startstopAI = new JCheckBox();
		startstopPassengerAmount = new JCheckBox();
		yellow = new JCheckBox();
		black = new JCheckBox();
		pink = new JCheckBox();
		purple = new JCheckBox();
		green = new JCheckBox();

		add(drawButton);
		drawButton.setVisible(false);

		setVisible(true);
		this.overviewpanel = overviewpanel;

		// drawStopAI();
		drawCheckBoxes();
		drawTimeSlider();
		drawAISlider();

		initLabels();
	}

	public void initLayout() {
		setLayout(null);
		setBounds(0, 530, 1024, 237);
		setVisible(true);
	}

	public void initLabels() {

		int i = 12;
		bischShuttles = new JLabel();
		aReisPass = new JLabel();
		totRijzShuttles = new JLabel();
		wPass = new JLabel();
		
	
		bischShuttles.setBounds(10, i, 250, 15);
		i += 15;
		this.add(this.bischShuttles);
		aReisPass.setBounds(10, i, 220, 15);
		i += 15;
		this.add(this.aReisPass);
		totRijzShuttles.setBounds(10, i, 220, 15);
		i += 15;
		this.add(this.totRijzShuttles);		
	}

	public void gegevensTekenen() {
		int total = 0;
		total += central.getStation(0).getShuttleAmount();
		total += central.getStation(1).getShuttleAmount();
		total += central.getStation(2).getShuttleAmount();
		total += central.getStation(3).getShuttleAmount();
		total += central.getStation(4).getShuttleAmount();
		total += central.getStation(5).getShuttleAmount();
		total += central.getStation(6).getShuttleAmount();
		total += central.getStation(7).getShuttleAmount();
		total += central.getStation(8).getShuttleAmount();
		bischShuttles.setText("Totaal aantal beschikbare shuttles = " + total);
		aReisPass.setText("Totaal Reizende passagiers = " + central.getDatabase().getPassengersTraveling());
		totRijzShuttles.setText("Totaal Rijdende shuttles = "
				+ (central.getDatabase().getShuttleOccupiedTraveling() + central.getDatabase()
						.getShuttlesEmptyTraveling()));

		
		/*wPass.setText("Aantal wachtende passagiers op: ");
		waiting0.setText("	-" + central.getStation(0).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(0)));
		waiting1.setText("	-" + central.getStation(1).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(1)));
		waiting2.setText("	-" + central.getStation(2).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(2)));
		waiting3.setText("	-" + central.getStation(3).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(3)));
		waiting4.setText("	-" + central.getStation(4).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(4)));
		waiting5.setText("	-" + central.getStation(5).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(5)));
		waiting6.setText("	-" + central.getStation(6).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(6)));
		waiting7.setText("	-" + central.getStation(7).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(7)));
		waiting8.setText("	-" + central.getStation(8).getStationName() + " = "
				+ central.getDatabase().getPassengersWaitingNow(central.getStation(8)));

				*/
	}

	public void drawCheckBoxes() {
		{
			startstopAI.setBounds(350, 20, 20, 20);
			startstopAI.setSelected(false);
			startstopAI.addActionListener(this);
			add(startstopAI);
		}
		{
			startstopAIlabel = new JLabel();
			startstopAIlabel.setText("Automatische reserveringen");
			startstopAIlabel.setBounds(370, 20, 150, 20);
			add(startstopAIlabel);
		}
		{
			startstopPassengerAmount.setBounds(350, 48, 20, 20);
			startstopPassengerAmount.setSelected(true);
			startstopPassengerAmount.addActionListener(this);
			add(startstopPassengerAmount);
		}
		{
			startstopPassengerAmountlabel = new JLabel();
			startstopPassengerAmountlabel.setText("Passagiers in Shuttle");
			startstopPassengerAmountlabel.setBounds(370, 48, 150, 20);
			add(startstopPassengerAmountlabel);
		}
		{
			yellow.setBounds(350, 76, 20, 20);
			yellow.setSelected(true);
			yellow.addActionListener(this);
			add(yellow);
		}
		{
			yellowLabel = new JLabel();
			yellowLabel.setText("Gele Shuttles");
			yellowLabel.setBounds(370, 76, 150, 20);
			add(yellowLabel);
		}
		{
			purple.setBounds(350, 104, 20, 20);
			purple.setSelected(true);
			purple.addActionListener(this);
			add(purple);
		}
		{
			purpleLabel = new JLabel();
			purpleLabel.setText("Paarse Shuttles");
			purpleLabel.setBounds(370, 104, 150, 20);
			add(purpleLabel);
		}
		{
			pink.setBounds(350, 132, 20, 20);
			pink.setSelected(true);
			pink.addActionListener(this);
			add(pink);
		}
		{
			pinkLabel = new JLabel();
			pinkLabel.setText("Roze Shuttles");
			pinkLabel.setBounds(370, 132, 150, 20);
			add(pinkLabel);
		}
		{
			black.setBounds(350, 160, 20, 20);
			black.setSelected(true);
			black.addActionListener(this);
			add(black);
		}
		{
			blackLabel = new JLabel();
			blackLabel.setText("Zwarte Shuttles");
			blackLabel.setBounds(370, 160, 150, 20);
			add(blackLabel);
		}
		{
			green.setBounds(350, 188, 20, 20);
			green.setSelected(true);
			green.addActionListener(this);
			add(green);
		}
		{
			greenLabel = new JLabel();
			greenLabel.setText("Groene Shuttles");
			greenLabel.setBounds(370, 188, 150, 20);
			add(greenLabel);
		}

	}

	public void drawButton(Reservation res, long tijd) {
		drawButton.setText("Inchecken binnen " + (tijd / (1000 * 60)) + " seconde");
		drawButton.setVisible(true);
	}

	public void removeButton(Reservation res) {
		drawButton.setVisible(false);
	}

	public void setCode(int c) {
		this.code = c;
	}

	public void drawAISlider() {
		aiSlider = new JSlider(0, 1, 100, central.getAI().getAgressie());
		aiSlider.setBounds(590, 15, 200, 25);
		aiSlider.setOpaque(false);
		add(aiSlider);

		bgAILabel.setBounds(590, 15, 200, 25);
		add(bgAILabel);

		aiLabel = new JLabel("Automatische reserverings drukte = " + central.getAI().getAgressie());
		aiLabel.setBounds(595, 40, 200, 25);
		add(aiLabel);
		aiSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				central.getAI().setAgression(aiSlider.getValue());
				aiLabel.setText("Automatische reserverings drukte = " + central.getAI().getAgressie());
			}
		});
	}

	public void drawTimeSlider() {
		timeSlider = new JSlider(0, 1, 500, central.getAcceleration());
		timeSlider.setBounds(590, 90, 200, 25);
		timeSlider.setOpaque(false);
		add(timeSlider);

		bgAccelerationLabel.setBounds(590, 90, 200, 25);
		add(bgAccelerationLabel);

		acceleration = new JLabel("Versnelling = x" + central.getAcceleration());
		acceleration.setBounds(650, 115, 100, 25);
		add(acceleration);
		timeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				central.setAcceleration(timeSlider.getValue());
				overviewpanel.updateTimer();
				acceleration.setText("Versnelling = x" + central.getAcceleration());
			}
		});
	}

	public void drawInlog() {
	new OwnTravelPanel3(central);
	}

	public JCheckBox getstartstopPassengerAmount() {
		return startstopPassengerAmount;
	}

	public ArrayList getShown() {
		ArrayList temp = new ArrayList();
		if (yellow.isSelected()) {
			temp.add(java.awt.Color.YELLOW);
		}
		if (pink.isSelected()) {
			temp.add(java.awt.Color.PINK);
		}
		if (purple.isSelected()) {
			temp.add(java.awt.Color.MAGENTA);
		}
		if (black.isSelected()) {
			temp.add(java.awt.Color.BLACK);
		}
		if (green.isSelected()) {
			temp.add(java.awt.Color.GREEN);
		}
		return temp;
	}

	private class incheckenPerron implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			drawInlog();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == startstopAI) {
			if (central.getAI().interruptAI());
		}
	}
}
