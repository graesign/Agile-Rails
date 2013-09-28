package railcabgui;

import java.awt.*;

public class SwitchNodeSprite extends NodeSprite {
	
	public SwitchNodeSprite( int _x, int _y, int size ) {
		this.size.setSize( size, size);
		this._x = _x;
		this._y = _y;
		this.selectable 	= true;
		this.isSwitchNode 	= true;
	}
	
	public SwitchNodeSprite( int _x, int _y, Dimension place, int size, int direction, int switchDirection ) {
		this._x = _x; this._y = _y; 
		this.place = place;
		this.size.setSize( size, size);
		this.direction = direction;
		this.switchDirection = switchDirection;
		this.selectable = true;
		this.isSwitchNode 	= true;
	}
	
	
	public void paint( Graphics g ) {
		super.paint( g );
		paintDirection( g, switchDirection );
		
		if( this.switched == false ) {
			this.next.paintSwitchDestination( g );
		}
		if( this.switched == true ) {
			this.switchNext.paintSwitchDestination( g );
		}
		
		
		
		Dimension d = this.drawParalel( 10, 10 );
		g.drawOval( d.width, d.height, 10, 10 );
		
		d = this.drawParalel( 12, 12 );
		g.drawOval( d.width, d.height, 12, 12 );
	}
	
	public void giveContent() {
		if( this.switched == false ){
			this.next.receiveContent( this.content );
		}
		else if( this.switched == true ) {
			this.switchNext.receiveContent( this.content );
		}
		this.content = null;
	}

}
