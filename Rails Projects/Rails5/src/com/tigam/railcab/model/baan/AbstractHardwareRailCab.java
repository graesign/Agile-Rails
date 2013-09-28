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

import com.tigam.railcab.model.exception.RailCabException;

/*
 * TODO: Deze class is niet volledig. Deze class is niet volledig. Voor de
 * implementatie van hardware railcabs zal in deze class alle
 * hardware-railcab-algemene code moeten worden gezet.
 */

/**
 * Implementatie van alle standaard onderdelen van een hardware RailCab. Alle
 * algemene code, nodig voor een RailCab die zowel in software als in hardware
 * bestaat. Een "hardware RailCab" extent deze klasse.
 * 
 * @author Arjan van der Velde
 * 
 */
public abstract class AbstractHardwareRailCab extends RailCab {

	/**
	 * Construct a RailCab op een gegeven locatie met een gegeven max aantal
	 * reizigers.
	 * 
	 * @param locatie het baandeel waar de railcab geplaatst moet worden
	 * @throws RailCabException
	 */
	public AbstractHardwareRailCab(Baandeel locatie) throws RailCabException {
		super(locatie);
	}

	/**
	 * Construct a RailCab op een gegeven locatie.
	 * 
	 * @param locatie het baandeel waar de railcab geplaatst moet worden
	 * @param maxReizigers het maximale aantal reizigers dat deze cab mag
	 *            bevatten
	 * @throws RailCabException
	 */
	public AbstractHardwareRailCab(Baandeel locatie, int maxReizigers) throws RailCabException {
		super(locatie, maxReizigers);
	}

}
