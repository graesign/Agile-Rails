package railcabgui;

import java.awt.*;

// To fill a 2d array with a user-defined build
public class TrackBuilder {
	
	// Fields
	private int squareSize, hSquares, vSquares;
	private RailCabSprite[][] sprites;
	private RailCabSprite	firstSprite;
	
	private static final int NORTH		= 1;
	private static final int EAST		= 2;
	private static final int SOUTH		= 3;
	private static final int WEST		= 4;
	private static final int NORTHEAST	= 5;
	private static final int SOUTHEAST  = 6;
	private static final int SOUTHWEST	= 7;
	private static final int NORTHWEST	= 8;
	
	// Constructor
	public TrackBuilder( int squareSize, int hSquares, int vSquares ) {
		// Load
		this.squareSize = squareSize; this.hSquares = hSquares; this.vSquares = vSquares;
		this.sprites 	= new RailCabSprite[hSquares][vSquares];
		// Init
		initSprites();
		
	// Select one of these to build
		build0();
		//build1();
		//build2();
		
		// Finalize
		linkNodes( this.firstSprite );
	}
	
	// linkNodes links all the nodes
	public void linkNodes( RailCabSprite sprite ) {
		if( sprite.direction == NORTH ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width][sprite.place.height - 1];
				linkNodes( this.sprites[sprite.place.width][sprite.place.height - 1] );
			}
		}
		if( sprite.direction == EAST ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width + 1][sprite.place.height];
				linkNodes( this.sprites[sprite.place.width + 1][sprite.place.height] );
			}
		}
		if( sprite.direction == SOUTH ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width][sprite.place.height + 1];
				linkNodes( this.sprites[sprite.place.width][sprite.place.height + 1] );
			}
		}
		if( sprite.direction == WEST ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width - 1][sprite.place.height];
				linkNodes( this.sprites[sprite.place.width - 1][sprite.place.height] );
			}
		}	
		if( sprite.direction == NORTHEAST ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width + 1][sprite.place.height - 1];
				linkNodes( this.sprites[sprite.place.width + 1][sprite.place.height - 1] );
			}
		}
		if( sprite.direction == SOUTHEAST ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width + 1][sprite.place.height + 1];
				linkNodes( this.sprites[sprite.place.width + 1][sprite.place.height + 1] );
			}
		}
		if( sprite.direction == SOUTHWEST ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width - 1][sprite.place.height + 1];
				linkNodes( this.sprites[sprite.place.width - 1][sprite.place.height + 1] );
			}	
		}
		if( sprite.direction == NORTHWEST ) {
			if( sprite.next == null ) {
				sprite.next = this.sprites[sprite.place.width - 1][sprite.place.height - 1];
				linkNodes( this.sprites[sprite.place.width - 1][sprite.place.height - 1] );
			}
		}
		// For SwitchNodes
		if( sprite.isSwitchNode == true ) {
			if( sprite.switchDirection == NORTH ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width][sprite.place.height - 1];
					linkNodes( this.sprites[sprite.place.width][sprite.place.height - 1] );
				}
			}
			if( sprite.switchDirection == EAST ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width + 1][sprite.place.height];
					linkNodes( this.sprites[sprite.place.width + 1][sprite.place.height] );
				}
			}
			if( sprite.switchDirection == SOUTH ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width][sprite.place.height + 1];
					linkNodes( this.sprites[sprite.place.width][sprite.place.height + 1] );
				}
			}
			if( sprite.switchDirection == WEST ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width - 1][sprite.place.height];
					linkNodes( this.sprites[sprite.place.width - 1][sprite.place.height] );
				}
			}	
			if( sprite.switchDirection == NORTHEAST ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width + 1][sprite.place.height - 1];
					linkNodes( this.sprites[sprite.place.width + 1][sprite.place.height - 1] );
				}
			}
			if( sprite.switchDirection == SOUTHEAST ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width + 1][sprite.place.height + 1];
					linkNodes( this.sprites[sprite.place.width + 1][sprite.place.height + 1] );
				}
			}
			if( sprite.switchDirection == SOUTHWEST ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width - 1][sprite.place.height + 1];
					linkNodes( this.sprites[sprite.place.width - 1][sprite.place.height + 1] );
				}	
			}
			if( sprite.switchDirection == NORTHWEST ) {
				if( sprite.switchNext == null ) {
					sprite.switchNext = this.sprites[sprite.place.width - 1][sprite.place.height - 1];
					linkNodes( this.sprites[sprite.place.width - 1][sprite.place.height - 1] );
				}
			}
		}
		 
	}
	
	// getSprites returns a (built) 2d array 
	public RailCabSprite[][] getSprites() {
		return this.sprites;
	}
	
	// replace replaces a Sprite at a given position
	private void replace( int collum, int row, RailCabSprite sprite ) {
		this.sprites[collum][row] = sprite;
	}
	
	public void build0() {
		insertNodeSprites( 10, 5, 25, EAST );
		insertSwitchNodeSprite( 25, 5, EAST, SOUTH );
		
		insertNodeSprites( 35, 5, 20, SOUTH );
		insertNodeSprites( 25, 6, 19, SOUTH );
		
		insertSwitchNodeSprite( 25, 14, SOUTH, EAST );
		insertNodeSprites( 26, 14, 9, EAST );
		
		insertNodeSprites( 35, 25, 25, WEST );
		insertNodeSprites( 10, 25, 20, NORTH );
		
		insertSwitchNodeSprite( 10, 14, NORTH, EAST );
		insertNodeSprites( 11, 14, 14, EAST );
		
		insertRailCab( 10, 5 );
		insertRailCab( 25, 5 );
		insertRailCab( 15, 25);
	}
	
	public void build1() {
		insertNodeSprites( 5, 5, 5, EAST );
		
		insertSwitchNodeSprite( 10, 5, EAST, SOUTHEAST );
		insertNodeSprites( 11, 5, 14, EAST );
		insertNodeSprites( 11, 6, 5, EAST );
		insertNodeSprites( 16, 6, 1, NORTHEAST );
		
		insertNodeSprites( 25, 5, 1, SOUTHEAST );
		insertNodeSprites( 26, 6, 5, SOUTH );
		
		insertSwitchNodeSprite( 26, 11, SOUTHEAST, SOUTHWEST );
		insertNodeSprites( 25, 12, 6, SOUTH );
		insertNodeSprites( 25, 18, 1, SOUTHEAST );
		insertNodeSprites( 27, 12, 6, SOUTH );
		insertNodeSprites( 27, 18, 1, SOUTHWEST );
		
		insertNodeSprites( 26, 19, 4, SOUTH );
		insertNodeSprites( 26, 23, 8, WEST );
		
		insertNodeSprites( 18, 23, 13, NORTHWEST );
		insertNodeSprites( 5, 10, 5, NORTH );
		
		insertSwitchNodeSprite( 14, 19, NORTHWEST, WEST );
		insertNodeSprites( 13, 19, 4, NORTHWEST );
		insertNodeSprites( 9, 15, 1, NORTH );
		
		
		insertRailCab( 5, 5 );
	}
	
	public void build2() {
		insertNodeSprites( 35, 5, 30, WEST );
		insertNodeSprites( 5, 5, 20, SOUTH );
		insertNodeSprites( 5, 25, 30, EAST );
		insertNodeSprites( 35, 25, 20, NORTH );
		insertRailCab( 35, 5 );
	}
	
	// insertNodeSprite inserts a NodeSprite
	// See NodeSprite.java to see which directions are supported
	public void insertNodeSprite( int collom, int row, int direction ) {
		int _x = collom	* squareSize;
		int _y = row	* squareSize;
		Dimension place = new Dimension( collom, row );
		NodeSprite node = new NodeSprite( _x, _y, place, squareSize, direction );
		replace( collom, row, node );
		
		// Set the first Node to construct the links from
		if( this.firstSprite == null ) {
			this.firstSprite = node;
		}
	}
	
	// insertNodeSprites lets you insert multiple Node's at once, in a certain direction
	public void insertNodeSprites( int collom, int row, int amount, int direction ) {
		if( direction == NORTH ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom, row - i, direction );
			}	
		}
		if( direction == EAST ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom + i, row, direction );
			}	
		}
		if( direction == SOUTH ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom, row + i, direction );
			}	
		}
		if( direction == WEST ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom - i, row, direction );
			}	
		}
		if( direction == NORTHEAST ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom + i, row - i, direction );
			}	
		}
		if( direction == SOUTHEAST ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom + i, row + i, direction );
			}	
		}
		if( direction == SOUTHWEST ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom - i, row + i, direction );
			}	
		}
		if( direction == NORTHWEST ) {
			for( int i = 0 ; i < amount; i++ ) {
				insertNodeSprite( collom - i, row - i, direction );
			}	
		}
	}
	
	// insertSwitchNodeSprite inserts a SwitchNodeSprite
	public void insertSwitchNodeSprite( int collom, int row, int direction, int switchDirection  ) {
		int _x = collom	* squareSize;
		int _y = row	* squareSize;
		Dimension place = new Dimension( collom, row );
		NodeSprite node = new SwitchNodeSprite( _x, _y, place, squareSize, direction, switchDirection );
		replace( collom, row, node );
		
	}
	
	// initSprites fills the 2d array with RailCabSprites
	public void initSprites() {
		for( int c = 0; c < this.hSquares; c++ ) {
			for( int r = 0; r < this.vSquares; r++ ) {
				this.sprites[c][r] = new RailCabSprite( c, r );
			}
		}
	}
	
	// insertRailCab inserts a RailCab
	public void insertRailCab( int collom, int row ) {
		this.sprites[collom][row].receiveContent( new RailCab() );
	}
	
}
