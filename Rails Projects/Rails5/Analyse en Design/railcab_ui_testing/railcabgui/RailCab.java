package railcabgui;

import java.awt.*;

public class RailCab extends RailCabSprite{
	
	public RailCab() {
		this.selectable = true;
		this.isRailCab 	= true;
	}
	
	public void paint( Graphics g ) {
		g.fillOval( this._x -1, this._y -1, this.size.width +1, this.size.height +1);
	}
	
	

}
