package railcabgui;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

// Canvas for painting and contains the other RailCabSprites
public class TrackCanvas extends Canvas implements MouseListener {

	// Fields
	private int squareSize, hSquares, vSquares;
	private RailCabSprite[][] sprites;
	private RailCabSprite	selection = new RailCabSprite();
	private ArrayList<RailCabSprite> railcabs = new ArrayList();
	
	// Constructor
	public TrackCanvas( int squareSize, int hSquares, int vSquares ) {
		// Parameters stored in the object's fields
		this.squareSize = squareSize; this.hSquares = hSquares; this.vSquares = vSquares;
		// Initialisation
		this.addMouseListener( this );
		this.setBackground( Color.white );
		
	}
	
	// Paint Methods
	public void paint( Graphics g ) {
		for( int c = 0; c < this.hSquares; c++ ) {
			for( int r = 0; r < this.vSquares; r++ ) {
				//System.out.println( this.sprites[r][c].wiebenik() );
				this.sprites[c][r].paint( g );
			}
		}
	}
	
	public void getRailCabs() {
		for( int c = 0; c < this.hSquares; c++ ) {
			for( int r = 0; r < this.vSquares; r++ ) {
				if( this.sprites[c][r].content != null ) {
					if( this.sprites[c][r].content.isRailCab == true ) {
						this.railcabs.add( this.sprites[c][r].content );
					}
				}
			}
		}
		
	}
	
	public void doUpdate() {
		for( RailCabSprite cab : railcabs ) {
			Dimension d = cab.place;
			this.sprites[d.width][d.height].giveContent();
		}
		
	}
	
	// loadSprites loads a 2d array to function as an onscreen grid
	public void loadSprites( RailCabSprite[][] sprites ) {
		this.sprites = sprites;
		this.getRailCabs();
	}
	
	// Selects the Sprite at a given coordinate
	public void selectSprite( int _x, int _y ) {
		int xNode = _x / squareSize;
		int yNode = _y / squareSize;
		if( xNode < this.hSquares && yNode < this.vSquares ) {
			this.selection.beSelected();
			this.sprites[xNode][yNode].beSelected();
			this.selection = this.sprites[xNode][yNode];
		}
	}
	
	public void getInfoClickedNode( int _x, int _y ) {
		int xNode = _x / squareSize;
		int yNode = _y / squareSize;
		System.out.println( "User clicked on coordinate [" + xNode + "][" + yNode + "]" );
	}
	
	// implements MouseListener Methods
	public void mouseClicked( MouseEvent e ) {
		//System.out.println( "Clicked at X/Y :" + e.getX() + "/" + e.getY() );
		selectSprite( e.getX(), e.getY() );
		getInfoClickedNode( e.getX(), e.getY() );
	}
	public void mouseEntered( MouseEvent e ) {
		//System.out.println( "Mouse entered TrackCanvas!" );
	}
	public void mousePressed( MouseEvent e ) {
	}
	public void mouseReleased( MouseEvent e) {
	}
	public void mouseExited( MouseEvent e ) {
	}
}
