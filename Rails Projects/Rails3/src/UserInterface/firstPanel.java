package UserInterface;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Business.Central;

import javax.swing.*;

public class firstPanel extends JFrame implements ActionListener {

	private JPanel panel;
	private JLabel introTekst, gegTekst, aantalShuttles;
	private JLabel amersfoortLabel, apeldoornLabel, zwolleLabel, deventerLabel, zutphenLabel, almeloLabel,
			hengeloLabel, enschedeLabel, oldenzaalLabel;
	private TextField amersfoortTF, apeldoornTF, zwolleTF, deventerTF, zutphenTF, almeloTF, hengeloTF, enschedeTF,
			oldenzaalTF;
	private JButton startButton;
	private String[] shutAmount;
	private JOptionPane popup;

	public firstPanel() {
		popup = new JOptionPane();
		panel = new JPanel();
		panel.setLayout(null);
		panel.setVisible(true);
		drawLabels();
		drawStationLabels();
		drawButton();
		drawTextFields();
		initValues();

		this.add(panel);

		this.setSize(270, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		shutAmount = new String[9];

	}

	public void initValues() {
		this.amersfoortTF.setText("" + (Central.stationDrukte[0]));
		this.apeldoornTF.setText("" + (Central.stationDrukte[1]));
		this.zwolleTF.setText("" + (Central.stationDrukte[2]));
		this.deventerTF.setText("" + (Central.stationDrukte[3]));
		this.zutphenTF.setText("" + (Central.stationDrukte[4]));
		this.almeloTF.setText("" + (Central.stationDrukte[5]));
		this.hengeloTF.setText("" + (Central.stationDrukte[6]));
		this.enschedeTF.setText("" + (Central.stationDrukte[7]));
		this.oldenzaalTF.setText("" + (Central.stationDrukte[8]));
	}

	public void drawLabels() {
		introTekst = new JLabel("Welkom bij Railcab.");
		introTekst.setBounds(10, 10, 150, 15);
		gegTekst = new JLabel("Vul de gegevens in en druk op start.");
		gegTekst.setBounds(10, 25, 300, 15);
		aantalShuttles = new JLabel("Vul in hoeveel shuttles elk station heeft:");
		aantalShuttles.setBounds(10, 50, 300, 15);
		panel.add(introTekst);
		panel.add(gegTekst);
		panel.add(aantalShuttles);
	}

	public void drawStationLabels() {
		amersfoortLabel = new JLabel("Amersfoort");
		this.amersfoortLabel.setBounds(10, 70, 100, 15);
		apeldoornLabel = new JLabel("Apeldoorn");
		this.apeldoornLabel.setBounds(10, 90, 100, 15);
		zwolleLabel = new JLabel("Zwolle");
		this.zwolleLabel.setBounds(10, 110, 100, 15);
		deventerLabel = new JLabel("Deventer");
		this.deventerLabel.setBounds(10, 130, 100, 15);
		zutphenLabel = new JLabel("Zutphen");
		this.zutphenLabel.setBounds(10, 150, 100, 15);
		almeloLabel = new JLabel("Almelo");
		this.almeloLabel.setBounds(10, 170, 100, 15);
		hengeloLabel = new JLabel("Hengelo");
		this.hengeloLabel.setBounds(10, 190, 100, 15);
		enschedeLabel = new JLabel("Enschede");
		this.enschedeLabel.setBounds(10, 210, 100, 15);
		oldenzaalLabel = new JLabel("Oldenzaal");
		this.oldenzaalLabel.setBounds(10, 230, 100, 15);

		panel.add(this.amersfoortLabel);
		panel.add(this.apeldoornLabel);
		panel.add(this.zwolleLabel);
		panel.add(this.deventerLabel);
		panel.add(this.zutphenLabel);
		panel.add(this.almeloLabel);
		panel.add(this.hengeloLabel);
		panel.add(this.enschedeLabel);
		panel.add(this.oldenzaalLabel);
	}

	public void drawTextFields() {
		this.amersfoortTF = new TextField(4);
		this.amersfoortTF.setBounds(130, 70, 30, 20);

		this.apeldoornTF = new TextField(4);
		this.apeldoornTF.setBounds(130, 90, 30, 20);

		this.zwolleTF = new TextField(4);
		this.zwolleTF.setBounds(130, 110, 30, 20);

		this.deventerTF = new TextField(4);
		this.deventerTF.setBounds(130, 130, 30, 20);

		this.zutphenTF = new TextField(4);
		this.zutphenTF.setBounds(130, 150, 30, 20);

		this.almeloTF = new TextField(4);
		this.almeloTF.setBounds(130, 170, 30, 20);

		this.hengeloTF = new TextField(4);
		this.hengeloTF.setBounds(130, 190, 30, 20);

		this.enschedeTF = new TextField(4);
		this.enschedeTF.setBounds(130, 210, 30, 20);

		this.oldenzaalTF = new TextField(4);
		this.oldenzaalTF.setBounds(130, 230, 30, 20);

		panel.add(this.amersfoortTF);
		panel.add(this.apeldoornTF);
		panel.add(this.zwolleTF);
		panel.add(this.deventerTF);
		panel.add(this.zutphenTF);
		panel.add(this.almeloTF);
		panel.add(this.hengeloTF);
		panel.add(this.enschedeTF);
		panel.add(this.oldenzaalTF);
	}

	public void drawButton() {
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setBounds(55, 300, 150, 30);
		panel.add(startButton);
		startButton.setVisible(true);
	}

	public boolean isValid() {
		for (String s : shutAmount) {
			if (s.length() >= 4) {
				return false;
			}
			try {
				Integer.parseInt(s);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {


		 try
		  {
	      	// ALLES ALA EIGEN OS STYLE:) WE HOUDEN NIET VAN JAVA LAYOUTJES
		 	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		  } catch(Exception e){}
			firstPanel fPanel = new firstPanel();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {

			shutAmount[0] = this.amersfoortTF.getText();
			shutAmount[1] = this.apeldoornTF.getText();
			shutAmount[2] = this.zwolleTF.getText();
			shutAmount[3] = this.deventerTF.getText();
			shutAmount[4] = this.zutphenTF.getText();
			shutAmount[5] = this.almeloTF.getText();
			shutAmount[6] = this.hengeloTF.getText();
			shutAmount[7] = this.enschedeTF.getText();
			shutAmount[8] = this.oldenzaalTF.getText();

			if (isValid()) {
				MainUI.main(shutAmount);
				this.setVisible(false);
			} else {
				popup
						.showMessageDialog(
								this,
								"Verkeerde input:\n- het aantal shuttles mag niet meer zijn dan 999\n- Let op dat u geen letters heeft ingevuld",
								"Fout", JOptionPane.ERROR_MESSAGE);

			}

		}

	}
}
