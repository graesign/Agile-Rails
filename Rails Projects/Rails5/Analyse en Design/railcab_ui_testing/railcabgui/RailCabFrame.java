package railcabgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// JFrame containing the picure
// It initializes most things and stars a thread to repaint everything
public class RailCabFrame extends JFrame implements Runnable {

	// Main Configuration
	private int 		squareSize  = 20;
	private int			hSquares	= 40;
	private int 		vSquares	= 30;
	
	// Fields
	private Dimension 	frameSize	= new Dimension();
	private Dimension 	trackSize	= new Dimension();
	private JPanel		trackpane	= new JPanel();
	private TrackCanvas railtrack;	
	
	// Constructor
	public RailCabFrame() {
		// Initialisation see bottom
		this.init();
		// Starts a Thread and refreshes the screen see below
		this.run();
	}
	
	
	// javax.swing Paint Methods
	public void paint( Graphics g ) {
		//this.paintComponents( g );
		this.trackpane.paint( g );
		this.railtrack.repaint();
	}
	
	public void update( Graphics g ) {
		//this.trackpane.paint( g );
		//this.railtrack.paint( g );
	}
	
	// implements Runnable Methods
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while (true) { 
		      this.railtrack.repaint();
		      this.railtrack.doUpdate();
		      try {
		    	  // Edit this
		    	  //System.out.println( "PING!" );
		    	  Thread.sleep (100);
		      }
		      catch (InterruptedException ex) {
		    	  
		      }
		      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}
	
	// initialisation
	private void init() {
		this.setTitle( "RailTrack tester");
		//Create a TrackCanvas
		this.railtrack = new TrackCanvas( this.squareSize, this.hSquares, this.vSquares );
		//Create a TrackBuilder
		TrackBuilder builder = new TrackBuilder( squareSize, hSquares, vSquares );
		this.railtrack.loadSprites( builder.getSprites() );
		
		int width 	= this.hSquares * squareSize;
		int height	= this.vSquares * squareSize;
		this.frameSize.setSize( width + 7, height + 26 );
		this.trackSize.setSize( width, height );
		
		this.setSize( this.frameSize );
		this.trackpane.setSize( this.trackSize );
		this.railtrack.setSize( this.trackSize ); 
		
		this.add( railtrack );
		this.setVisible( true );
		this.setResizable( false );
	}
	
	// Kelly houdt van Michiel
	
}
