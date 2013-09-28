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

package com.tigam.railcab.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;

/**
 * 
 * Een klasse met gestandaardieseerde methode om de baan op te slaan en weer te
 * openen
 * 
 * @author Nils Dijk
 * 
 */
public class BaanLoader {

	/**
	 * Een array van alle baandelen in een gegeven baan
	 * 
	 * @param baan de baan waar je alle baandelen van wilt hebben
	 * @return Een lijst met alle baandelen
	 */
	private static Baandeel[] getBaandelen(Baandeel baan) {
		ArrayList<Baandeel> baandelen = new ArrayList<Baandeel>();
		Stack<Baandeel> toParse = new Stack<Baandeel>();

		toParse.push(baan);

		Baandeel b;

		while (toParse.size() > 0) {
			b = toParse.pop();
			while (!baandelen.contains(b)) {
				baandelen.add(b);

				if (b.getClass().getName().equals(Wissel.class.getName())) {
					Wissel w = (Wissel) b;
					//if (!w.getStand()) try {w.zetOm();} catch (Exception ex) {}
					toParse.push(w.getAndere());
				}

				b = b.getVolgende();
			}
		}
		Object[] oa = baandelen.toArray();

		Baandeel[] ba = new Baandeel[oa.length];

		for (int i = 0; i < ba.length; i++) {
			ba[i] = (Baandeel) oa[i];
		}

		return ba;
	}

	/**
	 * Methode voor het makkelijk zoeken naar een index in een Array
	 * 
	 * @param oa De Array waarin gezocht moet worden
	 * @param o Het object wat gezocht moet worden
	 * @return de index van het object in de Array. -1 als het object niet
	 *         gevonden is
	 */
	private static int getIndexOf(Object[] oa, Object o) {
		for (int i = 0; i < oa.length; i++) {
			if (oa[i] == o) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Laad de baan met een gegeven InputStream
	 * 
	 * @param in De InputStream waar het bestand gevonden kan worden
	 * @return Een geladen baan object
	 */
	public static BaanHeader loadBaan(InputStream in) {
		if (in == null) {
			return null;
		}

		String content = "";

		int r;
		try {
			while ((r = in.read()) != -1) {
				content += (char) r;
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}

		String[] lines = content.split("\n");

		if (lines.length < 2) {
			return null;
		}

		String naam = lines[0];
		ArrayList<String> stationConstructors = new ArrayList<String>();
		HashMap<Integer, RailsConstructor> rcs = new HashMap<Integer, RailsConstructor>();
		int maxRc = 0;

		ArrayList<Station> stations = new ArrayList<Station>();
		ArrayList<Baandeel> baandelen = new ArrayList<Baandeel>();
		ArrayList<Wissel> wissels = new ArrayList<Wissel>();

		for (int i = 1; i < lines.length; i++) {
			String[] param = lines[i].split(" ", 2);

			if (param.length != 2) {
				return null;
			}
			if (param[0].equals("S")) {
				stationConstructors.add(param[1]);
			}
			if (param[0].equals("R")) {
				RailsConstructor rc = new RailsConstructor(param[1]);
				rcs.put(rc.getId(), rc);
				maxRc = Math.max(maxRc, rc.getId());
			}
		}

		for (int i = 0; i <= maxRc; i++) {
			RailsConstructor rc = rcs.get(i);
			Baandeel b = rc.getBaandeel();
			baandelen.add(b); // bewaar een platte lijst van baandelen
			if (b instanceof Wissel) {
				wissels.add((Wissel) b); // bewaar een platte lijst van baandelen
			}
			if (b instanceof Wissel && !(b instanceof WisselTerug)) {
				Wissel w = (Wissel) b;
				try {
					w.zetOm();
					w.setVolgende(rcs.get(rc.getAndere()).getBaandeel());
					w.zetOm();
				} catch (Exception ex) {
				}
			}

			Baandeel v = rcs.get(rc.getVolgende()).getBaandeel();
			b.setVolgende(v);

			if (v instanceof WisselTerug) {
				WisselTerug w = (WisselTerug) v;
				try {
					w.zetOm();
				} catch (Exception ex) {
				}
			}

		}

		for (int i = 0; i < stationConstructors.size(); i++) {
			String[] param = stationConstructors.get(i).split(" ", 3);
			StationBaandeel s = (StationBaandeel) rcs.get((new Integer(param[1])).intValue()).getBaandeel();
			if (s == null) {
				return null;
			}
			stations.add(new Station(param[2], s));
		}

		//System.out.println("Laad bestand " + filename + " (LIJNEN: " + lines.length + ")");
		//System.out.println("------------------------");
		//System.out.println(content);

		return new BaanHeader(naam, rcs.get(0).getBaandeel(), baandelen, wissels, stations);
	}

	/**
	 * Laad de baan met een gegeven bestandsnaam
	 * 
	 * @param filename De bestandsnaam van het baanbestand
	 * @return Een geladen baan object
	 */
	public static BaanHeader loadBaan(String filename) {
		return loadBaan(filename, false);
	}

	/**
	 * Laad de baan met een gegeven bestandsnaam
	 * 
	 * @param filename De bestandsnaam van het baanbestand
	 * @param fromJar moet het bestand uit het Jar bestand van het programma
	 *            komen?
	 * @return Een geladen baan object
	 */
	public static BaanHeader loadBaan(String filename, boolean fromJar) {
		InputStream in = ReadFile.read(filename, fromJar);
		return loadBaan(in);
	}

	/**
	 * Sla de baan op
	 * 
	 * @param baan De baan die opgeslagen moet worden
	 * @param naam De naam van de baan
	 * @param filename Een pad waar de baan opgeslagen kan worden
	 */
	public static void saveBaan(Baandeel baan, String naam, String filename) {
		String outp;

		ArrayList<Station> alStations = new ArrayList<Station>();

		String stations = null;
		String baandelen = null;
		String baandeel = "";
		String station = "";

		Baandeel[] ba = BaanLoader.getBaandelen(baan);

		for (int i = 0; i < ba.length; i++) {
			baandeel = "R " + i + " ";

			if (ba[i] instanceof WisselTerug) {
				baandeel += "T " + ba[i].getLocatie().x + " " + ba[i].getLocatie().y + " "
						+ getIndexOf(ba, ba[i].getVolgende()) + " " + (((WisselTerug) ba[i]).getStand() ? "1" : "0");
			} else if (ba[i] instanceof Wissel) {
				baandeel += "W " + ba[i].getLocatie().x + " " + ba[i].getLocatie().y + " "
						+ getIndexOf(ba, ba[i].getVolgende()) + " " + getIndexOf(ba, ((Wissel) ba[i]).getAndere())
						+ " " + (((Wissel) ba[i]).getStand() ? "0" : "0");
			} else if (ba[i] instanceof StationBaandeel) {
				if (!alStations.contains(((StationBaandeel) ba[i]).getStation())) {
					alStations.add(((StationBaandeel) ba[i]).getStation());
				}
				baandeel += "S " + ba[i].getLocatie().x + " " + ba[i].getLocatie().y + " "
						+ getIndexOf(ba, ba[i].getVolgende());
			} else {
				baandeel += "B " + ba[i].getLocatie().x + " " + ba[i].getLocatie().y + " "
						+ getIndexOf(ba, ba[i].getVolgende());
			}

			if (baandelen == null) {
				baandelen = "";
			} else {
				baandelen += "\n";
			}
			baandelen += baandeel;
		}

		for (int i = 0; i < alStations.size(); i++) {
			station = "S " + i + " " + getIndexOf(ba, alStations.get(i).getEersteBaandeel()) + " "
					+ alStations.get(i).getNaam();
			if (stations == null) {
				stations = "";
			} else {
				stations += "\n";
			}
			stations += station;
		}

		outp = naam;// + stations + "\n" + baandelen;
		if (stations != null) {
			outp += "\n" + stations;
		}
		if (baandelen != null) {
			outp += "\n" + baandelen;
		}

		char[] car = outp.toCharArray();

		try {
			File of = new File(filename);
			FileOutputStream fs = new FileOutputStream(of);
			for (char element : car) {
				fs.write(element);
			}
			fs.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		//System.out.println("---------------------------");
		//System.out.println(outp);
	}

}
