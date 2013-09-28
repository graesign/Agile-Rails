import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tigam.railcab.algoritme.BaanManager;
import org.tigam.railcab.algoritme.Reis;
import org.tigam.railcab.algoritme.ReisManager;
import org.tigam.railcab.algoritme.Reiziger;
import org.tigam.railcab.algoritme.Simulatie;

public class ReizerStat extends View {
	private JFrame frame;
	private JPanel rStats;
	private JLabel naam, reisTijd, wachtTijd;
	public int rTijd, wTijd, ID, minuut = 0, seconde=0;
	public String nReiziger, Str_wT, Str_rT;
	private ImageIcon bg;
	private JButton close;
	
	//krijg de waardes van TaxiStat en zet ze in variabele
	public ReizerStat(String nReiziger, String Str_wT, String Str_rT){
		this.nReiziger = nReiziger;
		this.ID = ID;
                this.Str_rT = Str_rT;
                this.Str_wT = Str_wT;
                maakGUIItems();
	}
	protected void maakGUIItems() {
            frame = new JFrame("Reiziger Statistieken"); 
            bg = new ImageIcon( "src/reiziger_stats.gif" );
            rStats = new JPanel(){
                protected void paintComponent(Graphics g)
                {
                        g.drawImage(bg.getImage(), 0, 0, null);
                        super.paintComponent(g);
                }
            };
            rStats.setOpaque( false );
            
            JComponent[] items = { naam = new JLabel(nReiziger), reisTijd = new JLabel (Str_rT + " minuten"), wachtTijd = new JLabel (Str_wT + " minuten") };
            Dimension[] lokaties = {new Dimension(20,60),new Dimension(20,200),new Dimension(20,125)};
            setGUIItemsLokaties( items, lokaties );
            setGUIItemsAfmetingen( items, new Dimension( 200, 100 ));
            setGUIItemsFont( items, new Font( "Arial", Font.BOLD, 16) );
            voegGUIItemsToe( frame.getContentPane(), items );
            
            frame.getContentPane().add(rStats, BorderLayout.CENTER);
            frame.setSize( 300, 300);
            frame.setVisible( true );
        }
        //inwendige klasse voor het afhandelen van het sluiten van het frame
        class Close implements ActionListener{
            public void actionPerformed(ActionEvent e){
                frame.dispose();
                }
            }
	
}
