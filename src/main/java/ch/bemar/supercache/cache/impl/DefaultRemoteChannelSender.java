package ch.bemar.supercache.cache.impl;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bemar.supercache.cache.IRemoteSenderChannel;
import ch.bemar.supercache.comm.ITransferContainer.OPERATION;
import ch.bemar.supercache.comm.impl.CacheClient;
import ch.bemar.supercache.comm.impl.TransferContainer;

public class DefaultRemoteChannelSender<K extends Serializable, V extends Serializable>
		implements IRemoteSenderChannel<K, V> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRemoteChannelSender.class);

	private final CacheClient<K, V> client;

	public DefaultRemoteChannelSender(CacheClient<K, V> client) {
		super();
		this.client = client;
	}

	public void send(K key, V value, OPERATION op) {
		try {
			client.sendMessage(new TransferContainer<>(key, value, op));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
