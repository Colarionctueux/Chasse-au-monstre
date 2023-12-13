package fr.univlille.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends MultiplayerBody {
	private static final int DEFAULT_PORT = 6666;
	private static Server instance = null;

	private ServerSocket server;
	private boolean acceptingNewUsers = true;
	private List<Socket> clientSockets = new ArrayList<>();
	private Runnable onIncomingCommunicationCallback;

	private Server() { }

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public static int getDefaultPort() {
		return DEFAULT_PORT;
	}

	/**
	 * Creates a server socket and listens to client messages in non-blocking threads.
	 * It will accept any incoming request as long as `Server.acceptingNewUsers` is `true` (which is its default value).
	 * The clients will need the hostname (the name of the phsyical machine) that this function is returning.
	 * @param port The port that the server will use for its communications.
	 */
	public void host(int port) {
		try {
			server = new ServerSocket(port);
			new Thread(() -> {
				try {
					while (acceptingNewUsers) {
						Socket clientSocket = server.accept();
						welcomeIncomingClient(clientSocket);

						new Thread(new ClientHandler(clientSocket)).start();
					}
				} catch (IOException e) {
					System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
					System.err.println(e.getMessage());
				}
			}).start();
		} catch (IOException e) {
			System.err.println("Exception caught when trying to initialize the server on port " + port);
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Hosts a game with the default port.
	 * @return The name of the host.
	 */
	public void host() {
		host(DEFAULT_PORT);
	}

	/**
	 * Stops new users from joining the server.
	 */
	public void closeRequests() {
		acceptingNewUsers = false;
	}

	/**
	 * Stops the server.
	 * The server has to be restarted (by calling `host()`) if it needs to be used again.
	 * This methods sets `this.server` to `null`.
	 * @throws IOException
	 */
	@Override
	public void kill() throws IOException {
		super.kill();
		for (Socket socket : clientSockets) {
			socket.close();
		}
		clientSockets.clear();
		server.close();
		server = null;
	}

	/**
	 * Checks if the server is running.
	 * If the server's running then the `ServerSocket` instance shouldn't be null.
	 * @return `true` if the server is running, `false` otherwise.
	 */
	@Override
	public boolean isAlive() {
		return server != null;
	}

	/**
	 * Sets the Runnable callback to execute when there is an incoming communication.
	 * It's very useful because if the server or the client are waiting for a communication,
	 * we don't want this action to block the main thread.
	 * @param callback
	 */
	public void setIncomingCommunicationCallback(Runnable callback) {
		onIncomingCommunicationCallback = callback;
	}

	/**
	 * Reset the callback that's executed when the server receive an incoming communication.
	 */
	public void stopIncomingCommunicationCallback() {
		onIncomingCommunicationCallback = null;
	}

	/**
	 * Sends a message to a client of the server using its output stream.
	 * @param socket  The socket instance of the client.
	 * @param message The message to send to the client.
	 * @throws IOException If, for some reason, the output stream of the client socket led to an Exception.
	 */
	public void writeToClient(Socket clientSocket, MultiplayerCommunication message) throws IOException {
		MultiplayerUtils.getOutputFromSocket(clientSocket).println(message.toString());
	}

	/**
	 * Sends a welcome message to the client, confirming its successfull connection.
	 * The server sends its name along with the message.
	 * @param clientSocket The socket of the incoming client.
	 * @throws IOException
	 */
	private void welcomeIncomingClient(Socket clientSocket) throws IOException {
		writeToClient(clientSocket, new MultiplayerCommunication(MultiplayerCommand.HOST, MultiplayerUtils.getHostname()));
		clientSockets.add(clientSocket);
	}

	/**
	 * Sends a message to all users that are listening to this server.
	 * @param message The message to broadcast.
	 */
	public void broadcast(MultiplayerCommunication message) throws IOException {
		for (Socket client : clientSockets) {
			writeToClient(client, message);
		}
	}

	private final class ClientHandler implements Runnable {
		private Socket clientSocket;

		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			try (
				PrintWriter out = MultiplayerUtils.getOutputFromSocket(clientSocket);
				BufferedReader in = MultiplayerUtils.getInputFromSocket(clientSocket);
			) {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					System.out.println("Server received: " + inputLine);
					try {
						MultiplayerCommunication incoming = new MultiplayerCommunication(inputLine);
						incomingBuffer.add(incoming);
						if (onIncomingCommunicationCallback != null) {
							onIncomingCommunicationCallback.run();
						}
					} catch (InvalidCommunicationException e) {
						// Invalid communications are ignored.
						System.err.println("Server received invalid communication : " + inputLine);
					}
				}
			} catch (IOException e) {
				System.err.println("Error handling client input");
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}