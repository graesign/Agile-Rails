/*-
 * Copyright (c) 2008 Nils Dijk, Michiel van den Anker, Oscar Orton,
 *  Tonny Wildeman, Arjan van der Velde
 *
 * TIGAM, Hogeschool van Amsterdam
 * http://www.tigam.com, http://home.ie.hva.nl
 * 
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * The name of the author may not be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tigam.railcab.model.simulatie;

import java.io.Serializable;

/**
 * Gesimuleerd date object. Gebruik deze om een Date-like object te krijgen met
 * als datum / tijd de interne simulatie datum / tijd.
 * 
 * @author Arjan van der Velde
 * 
 */
public class Date implements Serializable, Comparable<Date> {

	/** serializable... */
	private static final long serialVersionUID = 8398412286473910412L;

	/** de tijd waarop het laatste Date object is aangemaakt */
	private static long lastTime = 0;

	/** counter voor objecten aangemaakt op deze lastTime */
	private static long incrementer = 0;

	/**
	 * Geeft de huidige tijd van de simulatie terug
	 * 
	 * @return getTime() uit Simulatie.
	 */
	public static long getCurrentTime() {
		return Simulatie.getTime();
	}

	/** Tijd van dit Date object */
	private long time;

	/** Hoeveelste object in deze lastTime */
	private long order;

	/**
	 * Construct a new date... Set time to the current time.
	 */
	public Date() {
		time = Simulatie.getTime();
		order = incrementer;
		incrementer++;
		if (lastTime != time) {
			incrementer = 0;
		}
		lastTime = time;
	}

	/**
	 * How does this date compare to another date?
	 * 
	 * @param anotherDate the other date
	 * @return smaller, bigger, equal....
	 */
	public int compareTo(Date anotherDate) {
		if (anotherDate.time > time) {
			return 1;
		} else if (anotherDate.time < time) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * This this date equal another date?
	 * 
	 * @return yes or no
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Date) {
			Date d = (Date) obj;
			if (d.isSame(this) && d.getOrder() == getOrder()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the order
	 */
	public long getOrder() {
		return order;
	}

	/**
	 * Get the current time (in long format)
	 * 
	 * @return current time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Generate a hascode for this time. (Uses Long.hasCode()) (which might be a
	 * bit lame...)
	 */
	@Override
	public int hashCode() {
		return new Long(time).hashCode();
	}

	/**
	 * Controleert of objecten op hetzelfde tijdstip zijn aangemaakt.
	 * 
	 * @param d te vergelijken Date object
	 * @return gelijk of ongelijk
	 */
	public boolean isSame(Date d) {
		return d.getTime() == getTime();
	}

	/**
	 * Set the time...
	 * 
	 * @param time time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Return a string representation of this date. uses util.date.toString()
	 */
	@Override
	public String toString() {
		return "" + time;
	}

}
