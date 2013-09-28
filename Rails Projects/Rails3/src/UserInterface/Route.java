package UserInterface;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import Business.*;

public class Route {
	private ArrayDeque<Integer> x, y;
	private Station destination;
	private Shuttle shuttle;
	private java.awt.Color color;

	public Route(Station destination, Shuttle shuttle, java.awt.Color color) {
		this.destination = destination;
		this.shuttle = shuttle;
		this.color = color;

		x = new ArrayDeque<Integer>();
		y = new ArrayDeque<Integer>();
	}

	public void addCoordinates(int x, int y) {
		this.x.addLast(x);
		this.y.addLast(y);
	}

	public void addCoordinatesY(ArrayDeque<Integer> stack) {
		for (Integer i : stack) {
			this.y.addLast(i);
		}
	}

	public void addCoordinatesX(ArrayDeque<Integer> stack) {
		for (Integer i : stack) {
			this.x.addLast(i);
		}
	}

	public int getNextCoordinateY() {
		try {
			return this.y.pop();
		} catch (NoSuchElementException nsee) {
			return 0;
		}
	}

	public int getNextCoordinateX() {
		try {
			return this.x.pop();
		} catch (NoSuchElementException nsee) {
			return 0;
		}
	}

	public Station getDestination() {
		return destination;
	}

	public Shuttle getShuttle() {
		return shuttle;
	}

	public java.awt.Color getColor() {
		return color;
	}

}
