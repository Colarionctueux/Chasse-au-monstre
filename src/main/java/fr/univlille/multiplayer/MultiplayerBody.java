package fr.univlille.multiplayer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A multiplayer instance (the server, or a client) capable of receiving asynchronous messages.
 * Those messages need to be contained in a queue,
 * and the program will read them whenever it needs to.
 */
public abstract class MultiplayerBody {
	/**
	 * When a multiplayer body sends an asynchronous message to another,
	 * it stores it into a queue, waiting for the program to read them.
	 * Invalid communications are ignored.
	 */
	protected Queue<MultiplayerCommunication> incomingBuffer = new LinkedList<>();

	/**
	 * Checks if the buffer holding the pending requests isn't empty.
	 * @return `true` if there is no communication waiting to be read, `false` otherwise.
	 */
	public boolean hasPendingCommunication() {
		return !incomingBuffer.isEmpty();
	}

	/**
	 * Gets the oldest communication still pending.
	 * The communications come from the server,
	 * and the client holds them until the program reads them.
	 * @return An instance of `MultiplayerCommunication` or `null` if there is none.
	 */
	public MultiplayerCommunication pollCommunication() {
		return incomingBuffer.poll();
	}

	/**
	 * Deletes all pending communications.
	 */
	public void dropCommunications() {
		incomingBuffer.clear();
	}

	public abstract boolean isAlive();
	
	public void kill() throws IOException {
		dropCommunications();
	}
}
