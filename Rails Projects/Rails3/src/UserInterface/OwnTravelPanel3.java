package UserInterface;

import java.awt.*;
import java.awt.event.*;
import Business.Central;
import javax.swing.*;

public class OwnTravelPanel3 extends JFrame implements ActionListener {

	private JLabel voerCodeIn;
	private JLabel uBentOp;
	private GridLayout gLayout;
	private BorderLayout bLayout;
	private JPanel schermPanel;
	private JPanel knoppenPanel;
	private JPanel hoofdPanel;
	private JButton button0;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton button9;
	private JButton buttonC;
	private JButton buttonOK;
	private JTextField textField;
	private String code;
	private Central central;

	public OwnTravelPanel3(Central cntrl) {
		this.central = cntrl;
		this.setAlwaysOnTop(true);
		setSize(200, 315);
		setVisible(true);
		initLayout();
		drawButtons();

	}

	public void initLayout() {

		this.setLayout(null);
		gLayout = new GridLayout(4, 3, 5, 5);
		voerCodeIn = new JLabel("Voer uw code in:");
		uBentOp = new JLabel("U bent op het station");
		textField = new JTextField(4);
		knoppenPanel = new JPanel(gLayout);

		this.add(uBentOp);
		uBentOp.setBounds(30, 10, 200, 15);

		this.add(voerCodeIn);
		voerCodeIn.setBounds(45, 25, 100, 15);

		this.add(textField);
		textField.setBounds(75, 45, 35, 25);

		this.add(this.knoppenPanel);
		knoppenPanel.setBounds(20, 85, 150, 150);

	}

	public void drawButtons() {

		knoppenPanel.add(button1 = new JButton("1"));
		knoppenPanel.add(button2 = new JButton("2"));
		knoppenPanel.add(button3 = new JButton("3"));
		knoppenPanel.add(button4 = new JButton("4"));
		knoppenPanel.add(button5 = new JButton("5"));
		knoppenPanel.add(button6 = new JButton("6"));
		knoppenPanel.add(button7 = new JButton("7"));
		knoppenPanel.add(button8 = new JButton("8"));
		knoppenPanel.add(button9 = new JButton("9"));
		knoppenPanel.add(buttonC = new JButton("C"));
		knoppenPanel.add(button0 = new JButton("0"));
		knoppenPanel.add(buttonOK = new JButton("OK"));

		button0.addActionListener(this);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		button7.addActionListener(this);
		button8.addActionListener(this);
		buttonC.addActionListener(this);
		button9.addActionListener(this);
		buttonOK.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {

		if (textField.getText().length() < 4) {
			if (evt.getSource() == button0)
				textField.setText(textField.getText() + "0");

			if (evt.getSource() == button1)
				textField.setText(textField.getText() + "1");

			if (evt.getSource() == button2)
				textField.setText(textField.getText() + "2");

			if (evt.getSource() == button3)
				textField.setText(textField.getText() + "3");

			if (evt.getSource() == button4)
				textField.setText(textField.getText() + "4");

			if (evt.getSource() == button5)
				textField.setText(textField.getText() + "5");

			if (evt.getSource() == button6)
				textField.setText(textField.getText() + "6");

			if (evt.getSource() == button7)
				textField.setText(textField.getText() + "7");

			if (evt.getSource() == button8)
				textField.setText(textField.getText() + "8");

			if (evt.getSource() == button9)
				textField.setText(textField.getText() + "9");

			if (evt.getSource() == buttonOK) {
				code = textField.getText();
				if (Integer.parseInt(code) == central.getZelfreis().getCode()) {
					central.departZelfreis();
					this.dispose();

				} else
					new JOptionPane().showMessageDialog(this, "De code is onjuist probeer opnieuw!", "Fout",
							JOptionPane.ERROR_MESSAGE);

			}

		}
		if (evt.getSource() == buttonC) {
			try {
				textField.setText(textField.getText(0, textField.getText().length() - 1));
			} catch (Exception e) {
				System.out.println("textbox is leeg");
			}
		}
	}

}
