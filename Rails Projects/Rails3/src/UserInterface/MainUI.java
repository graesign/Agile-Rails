package UserInterface;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import Business.Central;
import javax.swing.event.*;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private StatisticsPanel statisticsPanel;
	private OverviewPanel overviewPanel;
	private InfoPanel infopanel;
	private StressTestPanel stresstest;
	private JPanel timePanel;
	private JTabbedPane tabPane;
	private Container container;
	private Central central;
	private OwnTravelPanel ownTravelPanel;

	public MainUI(String[] shutAmount) {
		super("Railcab");

		container = getContentPane();

		container.setLayout(null);

		tabPane = new JTabbedPane();

		statisticsPanel = new StatisticsPanel(this);
		central = new Central(this, shutAmount);

		overviewPanel = new OverviewPanel(central, this);
		infopanel = new InfoPanel(central, overviewPanel);
		stresstest = new StressTestPanel(central.getStationsArrayList(), central);
		ownTravelPanel = new OwnTravelPanel(central);

		timePanel = new JPanel();

		statisticsPanel.showGraph("Gemiddelde Shuttle bezetting", "Tijd", "Aantal", central.getDatabase().getAvgShuttleOccupationDS());
		
		statisticsPanel.showGraph("Reizende Passagiers", "Tijd", "Aantal", central.getDatabase()
				.getPassengersTravelingDS());
		statisticsPanel.showGraph("Lege Shuttles", "Tijd", "Aantal", central.getDatabase()
					.getEmptyShuttlesTravelingDS());

		this.setPreferredSize(new Dimension(1024, 767));
		setSize(1024, 767);

		container.add(tabPane);
		container.add(statisticsPanel);
		container.add(infopanel);

		// TAB PANE INIT BEGIN
		tabPane.addTab("Overview", null, overviewPanel, null);
		tabPane.addTab("Load testing", null, stresstest, null);
		tabPane.addTab("Zelf reizen", null, ownTravelPanel, null);

		tabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (tabPane.getSelectedComponent().equals(overviewPanel) && central.getSimDateOb().getPause())
					central.getSimDateOb().setPauseOff();
				if (tabPane.getSelectedComponent().equals(stresstest) && !central.getSimDateOb().getPause())
					central.getSimDateOb().setPauseOn();
				if (tabPane.getSelectedComponent().equals(ownTravelPanel) && !central.getSimDateOb().getPause())
					central.getSimDateOb().setPauseOn();

			}
		});

		tabPane.setDoubleBuffered(true);
		// TAB PANE INIT EIND

		tabPane.setBounds(0, 0, (int) (this.getWidth() * 0.78), (int) (this.getHeight() * 0.7));
		statisticsPanel.setBounds((int) (this.getWidth() * 0.78), 0, (int) (this.getWidth() * 0.22), (int) (this
				.getHeight() * 1.0));
		timePanel.setBounds((int) (this.getWidth() * 0.60), (int) (this.getHeight() * 0.7), 200, 50);
	}

	public void refreshInfoLabels() {
		if (infopanel != null)
			infopanel.gegevensTekenen();
	}

	public OverviewPanel getOverViewPanel() {
		return overviewPanel;
	}

	public OwnTravelPanel getOwnTravelPanel() {
		return ownTravelPanel;
	}

	public InfoPanel getInfoPanel() {
		return infopanel;
	}

	public static void main(String[] args) {
		try {
			// ALLES ALA EIGEN OS STYLE:) WE HOUDEN NIET VAN JAVA LAYOUTJES
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		MainUI mui = new MainUI(args);

		mui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mui.setVisible(true);

	}
}
