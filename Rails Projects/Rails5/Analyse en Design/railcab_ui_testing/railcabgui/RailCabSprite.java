package railcabgui;

import java.awt.*;
import java.awt.event.*;

// The base class 
public class RailCabSprite {
	
	// Fields
	protected 	static final int NORTH		= 1;
	protected 	static final int EAST		= 2;
	protected 	static final int SOUTH		= 3;
	protected 	static final int WEST		= 4;
	protected 	static final int NORTHEAST	= 5;
	protected 	static final int SOUTHEAST  = 6;
	protected 	static final int SOUTHWEST	= 7;
	protected 	static final int NORTHWEST	= 8;
	protected 	int direction;
	protected 	int switchDirection;
	protected	int _x;
	protected	int _y;
	protected	boolean selected;
	protected	boolean selectable;
	protected	boolean isNode 			= false;
	protected   boolean isSwitchNode 	= false;
	protected 	boolean isRailCab 		= false;
	protected 	Dimension size 		= new Dimension();
	protected 	Dimension offset 	= new Dimension();
	protected	Dimension place		= new Dimension();
	protected 	RailCabSprite next;
	protected 	RailCabSprite switchNext;
	protected 	RailCabSprite content;
	protected	boolean switched 		= false;
	
	// Constructor
	public RailCabSprite() {
		this.selected 	= false;
		this.selectable = false;
	}
	
	public RailCabSprite( int collom, int row ) {
		this.selected 	= false;
		this.selectable = false;
		this.place.width 	= collom;
		this.place.height 	= row;
	}
	
	// drawParalel gives you a x/y to draw something paralel to the center
	// Use it if you draw something bigger or smaller than the node
	public Dimension drawParalel( int width, int height ) {
		Dimension c = this.getCenter();
		this.offset.width 	= c.width 	- ( width 	/ 2 );
		this.offset.height 	= c.height 	- ( height 	/ 2 );
		return this.offset;
	}
	
	// getPlace returns the Sprite's position in the matrix/grid
	public Dimension getPlace() {
		return this.place;
	}
	
	//** init- and createOffset are obsolete with drawParalel 
	
	// initOffset creates an "offset" which is basicly a modulation to paint a Sprite from its center
	protected void initOffset( Dimension size )  {
		this.offset = this.createOffset( size );
	}
	
	// createOffset see initOffset
	protected Dimension createOffset( Dimension size ) {
		Dimension offset = new Dimension();
		offset.height 	= ( size.height	/ 2 );
		offset.width	= ( size.width	/ 2 );
		return offset;
	}
	
	// getCenter returns a Dimension containing the _x and _y cordinates of the Sprite's center
	// Could replace initOffset...
	protected Dimension getCenter() {
		Dimension d = new Dimension();
		d.width 	= this._x + ( this.size.width 	/ 2 );
		d.height 	= this._y + ( this.size.height 	/ 2 );
		return d;
	}
	
	// Paint Methods
	public void paint( Graphics g ) {
		if( this.content != null ) {
			this.content.paint( g );
		}
		
	}
	
	public void doUpdate() {
		if( this.content != null ) {
			this.giveContent();
		}
	}
	
	public void paintSwitchDestination( Graphics g ) {
		Dimension d = this.drawParalel( 10, 10 );
		g.drawRect( d.width, d.height, 10, 10 );
	}
	
	// beSelected selects a Sprite onscreen
	// If selected allready it will unselect else it will be selected if selectabe
	public void beSelected() {
		if( this.selected == true ) {
			this.selected = false;
			System.out.println( "deselected: " + this );
		}
		else if( this.selected == false ) {
			if( this.content != null ) {
				this.content.beSelected();
			}
			else if( this.selectable == true ) {
				System.out.println( "selected: " + this );
				System.out.println( "with next: " + this.next );
				if( this.isSwitchNode == true ) {
					this.flipSwitch();
					System.out.println( "with switchNext: " + this.switchNext );
				}
				this.selected = true;
			}
			
		}
	}
	
	public void receiveContent( RailCabSprite content ) {
		this.content = content;
		this.content.position( this._x, this._y, this.size, this.place );
	}
	
	public void position( int _x, int _y, Dimension size, Dimension place ) {
		this._x = _x; this._y = _y; this.size = size; this.place = place;
		System.out.println( this + " has been positioned at [" + this.place.width + "][" + this.place.height + "]" );
	}
	
	public void flipSwitch() {
		if( this.switched == false ) {
			this.switched = true;
		}
		else if( this.switched == true ) {
			this.switched = false;
		}
	}
	
	public void giveContent() {
	}
	
}
