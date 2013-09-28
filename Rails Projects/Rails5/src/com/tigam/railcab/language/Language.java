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

package com.tigam.railcab.language;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Een verzameling van methode voor het laden van taalgebonden onderdelen
 * 
 * @author eclipse
 * 
 */
public class Language {
	/**
	 * De path naar alle taal bundels
	 */
	private static String BUNDLE_NAME_PATH = "com.tigam.railcab.language.";

	/**
	 * De naam van de huidige bundle
	 */
	private static String BUNDLE_NAME = BUNDLE_NAME_PATH + "messages"; //$NON-NLS-1$

	/**
	 * De bundel waar de taal uit gehaald kan worden
	 */
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * De huidige taal
	 */
	private static Lang lang = null;

	/**
	 * De beschikbare talen
	 */
	private static Lang[] talen = { new Lang("Nederlands", "nl"), new Lang("English", "en") };

	/**
	 * Een lijst met alle geïntereseerde voor als de taal verandert
	 */
	private static ArrayList<LanguageListener> listeners = new ArrayList<LanguageListener>();

	/**
	 * Voeg een LanguageListener toe aan de lijst met listeners als deze er nog
	 * niet op staat
	 * 
	 * @param listener de Listener die moet worden toegevoegt
	 */
	public static void addLanguageListener(LanguageListener listener) {
		if (listeners.contains(listener)) {
			return;
		}
		listeners.add(listener);
	}

	/**
	 * Vraag de huidige taal op
	 * 
	 * @return de huidige taal
	 */
	public static Lang getCurrentLang() {
		if (lang == null) {
			if (BUNDLE_NAME.equals("com.tigam.railcab.language.messages")) {
				lang = new Lang("Nederlands", "nl");
			}
			if (BUNDLE_NAME.equals("com.tigam.railcab.language.messages_en")) {
				lang = new Lang("English", "en");
			}
			if (BUNDLE_NAME.equals("com.tigam.railcab.language.messages_nl")) {
				lang = new Lang("Nederlands", "nl");
			}
		}
		return lang;
	}

	/**
	 * Vraag een lijst op met bijbehorende talen
	 * 
	 * @return een Array met Lang objecten, elk element stelt een adnere taal
	 *         voor
	 */
	public static Lang[] getLanguages() {
		return talen;
	}

	/**
	 * Vraag een tekst op in de huidige taal aan de hand van de key
	 * 
	 * @param key de key van de tekst
	 * @return de tekst in de huidige taal
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Geef alle LanguageListeners een seintje dat de taal verandert is
	 */
	private static void notifyLanguageListeners() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).LanguageChanged();
		}
	}

	/**
	 * Verwijder een LanguageListener uit de lijst met Listeners
	 * 
	 * @param listener de LanguageListener die verwijderd moet worden
	 * @return <b>true</b> - listener is verwijderd<br>
	 *         <b>false</b> - listener is niet verwijderd
	 */
	public static boolean removeLanguageListener(LanguageListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Verander de taal van het programma
	 * 
	 * @param lang taal waar het programma naar moet veranderen
	 */
	public static void setLanguage(Lang lang) {
		BUNDLE_NAME = BUNDLE_NAME_PATH + "messages";

		if (lang != null) {
			BUNDLE_NAME += "_" + lang.getCode();
		}

		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException e) {
			setLanguage(null);
			return;
		}

		Language.lang = lang;
		notifyLanguageListeners();
	}
}
