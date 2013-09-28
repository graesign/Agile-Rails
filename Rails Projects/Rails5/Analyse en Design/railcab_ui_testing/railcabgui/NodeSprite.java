package railcabgui;

import java.awt.*;

// The NodeSprite
public class NodeSprite extends RailCabSprite {
	
	
	// Constructor
	public NodeSprite() {
	}
	
	public NodeSprite( int _x, int _y, Dimension place, int size, int direction ) {
		this._x = _x; this._y = _y; 
		this.place = place;
		this.size.setSize( size, size);
		this.direction = direction;
		//this.initOffset( size );
		this.selectable = true;
	}
	
	// setNext
	public void setNext( RailCabSprite sprite ) {
		this.next = sprite;
	}
	
	// Paint Methods
	public void paint( Graphics g ) {
		paintDirection( g, this.direction );
		if( this.content != null ) {
			super.paint( g );
		}
		else {
			g.drawOval(this._x, this._y, this.size.width, this.size.height );
			// If the NodeSprite is selected, it is highlighted by a square
			if( this.selected == true ) {
				g.drawRect( this._x, this._y, this.size.width, this.size.height );
			}
		}
	}
	
	// paintDirection draws a line from the Node's center to the next Sprite in that direction
	public void paintDirection( Graphics g, int direction ) {
		Dimension center = getCenter();
		if( direction == NORTH ) {
			g.drawLine( center.width, center.height, center.width, center.height - this.size.height );
		}
		if( direction == EAST ) {
			g.drawLine( center.width, center.height, center.width + this.size.width, center.height );
		}
		if( direction == SOUTH ) {
			g.drawLine( center.width, center.height, center.width, center.height + this.size.height );
		}
		if( direction == WEST ) {
			g.drawLine( center.width, center.height, center.width - this.size.width, center.height );
		}
		if( direction == NORTHEAST ) {
			g.drawLine( center.width, center.height, center.width + this.size.width, center.height - this.size.height );
		}
		if( direction == SOUTHEAST ) {
			g.drawLine( center.width, center.height, center.width + this.size.width, center.height + this.size.height );
		}
		if( direction == SOUTHWEST ) {
			g.drawLine( center.width, center.height, center.width - this.size.width, center.height + this.size.height );
		}
		if( direction == NORTHWEST ) {
			g.drawLine( center.width, center.height, center.width - this.size.width, center.height - this.size.height );
		}
		
	}
	
	public void giveContent() {
		if( this.next.content == null ) {
			this.next.receiveContent( this.content );
			this.content = null;
		}
	}
		
}
