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

package com.tigam.railcab.model.navigatie;

import java.util.HashMap;

/**
 * HashMap met twee keys
 * 
 * @author Arjan van der Velde
 * 
 * @param <A> key 1
 * @param <B> key 2
 * @param <C> opgeslagen value
 */
public class TriHashMap<A, B, C> {

	/** Interne opslag d.m.v. twee HashMaps */
	private HashMap<A, HashMap<B, C>> triMap;

	/**
	 * Creeer de TriHashMap
	 */
	public TriHashMap() {
		triMap = new HashMap<A, HashMap<B, C>>();
	}

	/**
	 * Bevat deze map een entry met de gegeven twee bijbehorende keys?
	 * 
	 * @param a key 1
	 * @param b key 2
	 * @return ja of nee
	 */
	public boolean containsKeys(A a, B b) {
		synchronized (triMap) {
			return triMap.containsKey(a) && triMap.get(a).containsKey(b);
		}
	}

	/**
	 * Geef de entry voor twee gegeven keys
	 * 
	 * @param a key 1
	 * @param b key 2
	 * @return de entry
	 */
	public C get(A a, B b) {
		synchronized (triMap) {
			if (triMap.containsKey(a)) {
				return triMap.get(a).get(b);
			} else {
				return null;
			}
		}
	}

	/**
	 * Sla een entry op met bijbehorende keys
	 * 
	 * @param a key 1
	 * @param b key 2
	 * @param c value van de entry
	 */
	public void put(A a, B b, C c) {
		synchronized (triMap) {
			if (triMap.containsKey(a)) {
				triMap.get(a).put(b, c);
			} else {
				HashMap<B, C> h = new HashMap<B, C>();
				h.put(b, c);
				triMap.put(a, h);
			}
		}
	}

	//	/**
	//	 * Geef het aantal entries voor deze map (size() van de buitense HashMap)
	//	 * @return size van deze map.
	//	 */
	//	public int size() {
	//		return triMap.size();
	//	}

}
