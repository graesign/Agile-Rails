import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;

public class About extends Frame{

	private JFrame frame;
	private ImageIcon bg;
	private JPanel about;

	public About(){
		bg = new ImageIcon("src/Over_ons.gif");
		frame = new JFrame("Over Ons");
		about = new JPanel(){
			protected void paintComponent(Graphics g)
			{
				g.drawImage(bg.getImage(), 0, 0, null);
				super.paintComponent(g);
			}
		};

		frame.addWindowListener( new Sluiten());

		about.setOpaque( false );
		frame.getContentPane().add(about, BorderLayout.CENTER);
		frame.setSize( 300, 300);
		frame.setVisible( true );
	}

	//inwendige klasse voor het correct afhandelen van het sluiten van het frame
	private class Sluiten extends WindowAdapter {
		public void windowClosing( WindowEvent e){
			frame.dispose();

		}
	}

}


