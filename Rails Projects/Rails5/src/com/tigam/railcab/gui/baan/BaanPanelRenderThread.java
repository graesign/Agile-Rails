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

package com.tigam.railcab.gui.baan;

/**
 * 
 * Om voor een vloeiende weergave van de het BaanPanel te zorgen een appart
 * Thread die elke 40 ms ( is 24 keer per seconde ) de repaint() aanroept.
 * 
 * @author Nils Dijk
 * 
 */

public class BaanPanelRenderThread extends Thread {

	/**
	 * Baanpanel wat opnieuw getekent moet worden
	 */
	private BaanPanel p;

	/**
	 * geeft aan of hij nog moet tekenen
	 */
	private boolean running;

	/**
	 * Constructor, initializeer het render thread door aantegeven op welk
	 * BaanPanel er een repaint moet worden uitgevoerd.
	 * 
	 * @param p het BaanPanel dat een framerate van 24 wil hebben :)
	 */
	public BaanPanelRenderThread(BaanPanel p) {
		this.p = p;
		running = true;
		start();
	}

	/**
	 * kill het Thread
	 */
	public void kill() {
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		int count = 0;
		while (running) {
			p.userRepaint();
			try {
				Thread.sleep(40);
				count++;
				count %= 25;
			} catch (Exception e) {
				System.out.println(e);
			}
			if (count == 0) {
				p.repaint();
			}
		}
	}

}
