/**
 * In deze klasse word de gebruiker laten zien welke reservering hij/zij gemaakt heeft.
 * ook krijgt de gebruiker zijn code.
 */

package UserInterface;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import Business.Central;
import Data.Reservation;
import javax.swing.*;

public class OwnTravelPanel2 extends JPanel implements ActionListener {

	private JLabel tekstGegevens, vStation, aStation, aTijd, labelCode;
	private Central central;
	private Reservation reservation;
	private OwnTravelPanel otp;
	private String stringTijd;
	private GridBagLayout gbLayout;
	private Font font;
	private int code;

	public OwnTravelPanel2(OwnTravelPanel otp, Central central) {
		this.initLayout();
		this.central = central;
		this.otp = otp;
		code = otp.getCode();
		reservation = central.getReservationCK(code);
		showData();

	}

	public void initLayout() {
		gbLayout = new GridBagLayout();

		gbLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
		gbLayout.rowHeights = new int[] { 3, 3, 3, 3 };
		gbLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
		gbLayout.columnWidths = new int[] { 3, 3, 3, 3 };

		this.setLayout(gbLayout);
	}

	public void showData() {
		vStation = new JLabel();
		aStation = new JLabel();
		aTijd = new JLabel();
		labelCode = new JLabel();
		tekstGegevens = new JLabel("UW GEGEVENS:");

		int intTijdUur = reservation.getDepartTime().getTime().getHours();
		int intTijdMin = reservation.getDepartTime().getTime().getMinutes();
		stringTijd = ("Uw vertrektijd is:     " + intTijdUur + " uur " + intTijdMin);

		vStation.setText("Uw vertrekstation is:     " + reservation.getDepartStation().getStationName());
		aStation.setText("Uw aankomststation is:     " + reservation.getArrivalStation().getStationName());
		aTijd.setText(stringTijd);
		labelCode.setText("uw code is:     " + code);

		this.add(tekstGegevens, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(vStation, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(aStation, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(aTijd, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(labelCode, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	}

	public void actionPerformed(ActionEvent evt) {

	}

}
