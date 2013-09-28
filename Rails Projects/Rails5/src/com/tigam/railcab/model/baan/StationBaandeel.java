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

package com.tigam.railcab.model.baan;

import java.awt.Point;

/**
 * Stations zijn opgebouwd uit dit type baandeel.
 * 
 * @author Arjan van der Velde
 * 
 */
public class StationBaandeel extends Baandeel {

	/** station waarbij dit baandeel hoort */
	private Station station;

	/**
	 * Creeer een StationBaandeel op gegeven locatie
	 * 
	 * @param locatie locatie waarop het baandeel gecreeerd dient te worden
	 */
	public StationBaandeel(Point locatie) {
		this(locatie, null);
	}

	/**
	 * Creeer een StationBaandeel op gegeven locatie, behorende bij gegeven
	 * station.
	 * 
	 * @param locatie locatie waarop het baandeel gecreeerd dient te worden
	 * @param station station waarbij dit baandeel hoort.
	 */
	public StationBaandeel(Point locatie, Station station) {
		super(locatie);
		this.station = station;
	}

	/**
	 * Geef het bijbehorende station
	 * 
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * Set het bijbehorende station. Maak ook aangrenzende station-baandelen
	 * consistent met deze setter.
	 * 
	 * @param station the station to set
	 */
	public void setStation(Station station) {
		this.station = station;
		// maak baandeel <-> station consistent
		if (!station.getBaandelen().contains(this)) {
			station.addBaandeel(this);
		}
		// Als vorige naar geen of verkeerd station wijst, aanpassen
		if (getVorige() instanceof StationBaandeel && ((StationBaandeel) getVorige()).getStation() != getStation()) {
			((StationBaandeel) getVorige()).setStation(getStation());
		}
		// Als volgende naar geen of verkeerd station wijst, aanpassen
		if (getVolgende() instanceof StationBaandeel && ((StationBaandeel) getVolgende()).getStation() != getStation()) {
			((StationBaandeel) getVolgende()).setStation(getStation());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.Baandeel#setVolgende(model.Baandeel)
	 */
	@Override
	public void setVolgende(Baandeel volgende) {
		super.setVolgende(volgende);
		// maak relatie station <=> baandeel consistent
		if (volgende instanceof StationBaandeel) {
			StationBaandeel baandeel = (StationBaandeel) volgende;
			if (getStation() != null && baandeel.getStation() != getStation()) {
				baandeel.setStation(getStation());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.Baandeel#setVorige(model.Baandeel)
	 */
	@Override
	public void setVorige(Baandeel vorige) {
		super.setVorige(vorige);
		// maak relatie station <=> baandeel consistent
		if (vorige instanceof StationBaandeel) {
			StationBaandeel baandeel = (StationBaandeel) vorige;
			if (getStation() != null && baandeel.getStation() != getStation()) {
				baandeel.setStation(getStation());
			}
		}
	}

}
