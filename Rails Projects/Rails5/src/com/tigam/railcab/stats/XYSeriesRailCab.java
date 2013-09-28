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

package com.tigam.railcab.stats;

import java.util.ArrayList;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

/**
 * This class is an extension of {@link XYSeries} to have the series maintain a
 * domain of data
 * 
 * @author Tonny Wildeman
 * 
 */
public class XYSeriesRailCab extends XYSeries {

	/** For serialization */
	private static final long serialVersionUID = 6466431527373345993L;

	/** The window size of the series */
	private double windowSize = Double.MAX_VALUE;

	/**
	 * Constructor for the series Always autoSorted and duplicates are allowed
	 * within this class
	 * 
	 * @param key - the <code>String</code> representing the name of the
	 *            series
	 */
	public XYSeriesRailCab(Comparable<String> key) {
		super(key, true, true);
		data = new ArrayList<XYDataItem>();
	}

	@Override
	public void add(double x, double y) {
		super.add(x, y);
		while (((XYDataItem)data.get(0)).getX().doubleValue() < ((XYDataItem)data.get(data.size() - 1)).getX().doubleValue() - windowSize) {
			data.remove(0);
		}
	}

	@Override
	public void add(double x, double y, boolean notify) {
		super.add(x, y, notify);

		while (((XYDataItem)data.get(0)).getX().doubleValue() < ((XYDataItem)data.get(data.size() - 1)).getX().doubleValue() - windowSize) {
			data.remove(0);
		}
	}

	/**
	 * Defines the window, domaining from the largest x-value to the largest
	 * x-value minus the <code>this.windowSize</code> Assumes always
	 * autoSorted and no duplicates allowed
	 * 
	 * @param windowSize - the size of the window
	 */
	public void setWindowSize(double windowSize) {
		this.windowSize = windowSize;

		if (data.size() != 0) {
			while (((XYDataItem)data.get(0)).getX().doubleValue() < ((XYDataItem)data.get(data.size() - 1)).getX().doubleValue() - windowSize) {
				data.remove(0);
			}
		}
	}
}
