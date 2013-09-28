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

package com.tigam.railcab.gui;

import java.awt.Color;

/**
 * Alle in de user interface gebruikte kleuren.
 * 
 * @author Nils Dijk
 * 
 */
public class Kleuren {

	/* TIGAM kleuren */
	/**
	 * De lichtblauwe kleur van Tigam
	 */
	public static final Color TIGAM_LICHTBLAUW = new Color(0x66, 0x99, 0xFF);

	/**
	 * De donkerblauwe kleur van Tigam
	 */
	public static final Color TIGAM_DONKERBLAUW = new Color(0x00, 0x33, 0x99);

	/* Achtergrond */
	/**
	 * Achtergrond van de simulatie
	 */
	public static Color Achtergrond = null;

	/**
	 * Achtergrond van de editor
	 */
	public static Color AchtergrondEditor = new Color(0xEE, 0xEE, 0xEE); // licht grijs

	/* Baandelen */
	/**
	 * De kleuren van het lijntje om de baandelen
	 */
	public static Color Baandeel = Color.BLACK;

	/**
	 * Kleur van een nieuw baandeel in de editor
	 */
	public static Color BaandeelNieuw = Color.GRAY;

	/**
	 * Kleur van de achtergrond van een baandeel
	 */
	public static Color BaandeelAchtergrond = Color.WHITE;

	/**
	 * De kleur van een geselecteer baandeel
	 */
	public static Color BaandeelSelected = Color.GREEN;

	/**
	 * De kleur die het stoplicht heeft
	 */
	public static Color BaandeelStoplicht = Color.RED;

	/* Filter */
	/**
	 * Kleur van het Baan Gebruik Filter
	 */
	public static Color FilterBaanGebruik = Color.GREEN;

	/**
	 * Kleur van het Station Drukte Filter
	 */
	public static Color FilterStation = Color.RED;

	/* Stations */
	/**
	 * Kleur van een normaal station
	 */
	public static Color Station = Color.BLACK;

	/**
	 * Kleur van een nieuw Station
	 */
	public static Color StationNieuw = Color.GRAY;

	/**
	 * Kleur van een geselecteerd station
	 */
	public static Color StationSelected = Color.GREEN;

	/* RailCabs */
	/**
	 * Kleur van een lege trein
	 */
	public static Color TreinLeeg = TIGAM_LICHTBLAUW;

	/**
	 * Kleur van een volle trein
	 */
	public static Color TreinVol = TIGAM_DONKERBLAUW;

	/* Wissels */
	/**
	 * Kleur van een standaar wissel
	 */
	public static Color Wissel = Color.ORANGE;

	/**
	 * Kleur van een nieuw Wissel
	 */
	public static Color WisselNieuw = Color.ORANGE;

	/* Splash */
	/**
	 * Kleur van de achtergrond van de banner in het Splash Screen
	 */
	public static Color SplashBanner = TIGAM_LICHTBLAUW;

	/**
	 * Kleur van de Achtergrond van het 'werkgedeelte' in het Splash Screen
	 */
	public static Color SplashBackground = Color.WHITE;

}
