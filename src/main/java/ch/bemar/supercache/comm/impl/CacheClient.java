package ch.bemar.supercache.comm.impl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bemar.supercache.comm.ITransferContainer;

public class CacheClient<K extends Serializable, V extends Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheClient.class);

	private final String other;

	public CacheClient(String other) {

		this.other = other;

		LOGGER.info("Initializing {} for other client {}", this.getClass().getName(), this.other);
	}

	public boolean sendMessage(ITransferContainer<K, V> container) throws IOException {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(getHost(), getPort());
			clientSocket.setSoTimeout(2000);

			LOGGER.debug("Sending");
			String answer = serializeAndSend(container, clientSocket);
			LOGGER.debug("Got answer: {}", answer);

		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (clientSocket != null && !clientSocket.isClosed())
				clientSocket.getOutputStream().close();
		}

		return true;
	}

	private String serializeAndSend(ITransferContainer<K, V> container, Socket socket) throws IOException {

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

		LOGGER.debug("Sending object {}", container);

		objectOutputStream.writeObject(container);
		objectOutputStream.flush();

		LOGGER.debug("Waiting for answer");
		String answer = IOUtils.toString(socket.getInputStream(), StandardCharsets.UTF_8);

		LOGGER.debug("Answer: {}", answer);

		return answer;
	}

	private Integer getPort() {
		return Integer.parseInt(StringUtils.substringAfter(other, ":"));
	}

	private String getHost() {
		return StringUtils.substringBefore(other, ":");
	}

//	public static final void main(String[] args) throws IOException, InterruptedException {
//		CacheClient cc = new CacheClient<>();
//		cc.init("localhost:6666");
//
//		for (int i = 0; i < 10; i++) {
//
//			Date now = new Date();
//
//			TransferContainer<Integer, String> tc = new TransferContainer<>(i, "Now its " + now);
//
//			cc.sendMessage(tc);
//
//			Thread.currentThread().sleep(650);
//
//		}
//	}
}