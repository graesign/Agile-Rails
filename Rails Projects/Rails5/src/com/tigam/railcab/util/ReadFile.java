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
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Een hulpmiddel bij het laden van bestanden die nu nog op schijf staan maar
 * straks mee moeten in het Jar bestand
 * 
 * @author Nils Dijk
 * 
 */
public class ReadFile {

	/**
	 * Open een bestand wat mischien in de Jar staat en anders op de schijf te
	 * vinden is
	 * 
	 * @param filename bestandsnaam
	 * @return Een InputStream naar het gevraagde bestand
	 */
	public static InputStream read(String filename) {
		InputStream in;
		in = read(filename, true);
		if (in != null) {
			return in;
		}
		in = read(filename, false);
		return in;
	}

	/**
	 * Open een bestand met de gegeven bestandsnaam.
	 * 
	 * @param filename bestandsnaam
	 * @param fromJar moet er gezocht worden in de jar of op de schijf
	 * @return Een InputStream naar het gevraagde bestand
	 */
	public static InputStream read(String filename, boolean fromJar) {
		if (fromJar) {
			try {
				return ReadFile.class.getResourceAsStream("/" + filename);
			} catch (Exception ex) {
			}
			return null;
		} else {
			try {
				return new FileInputStream(new File(filename));
			} catch (Exception e) {
			}
			return null;
		}
	}

}
