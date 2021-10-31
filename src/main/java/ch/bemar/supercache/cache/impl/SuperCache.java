package ch.bemar.supercache.cache.impl;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import ch.bemar.supercache.cache.IPersistenceChannel;
import ch.bemar.supercache.comm.IRemoteSenderChannel;
import ch.bemar.supercache.comm.ITransferContainer.OPERATION;

public class SuperCache<K extends Serializable, V extends Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuperCache.class);

	private final TheCache<K, V> cache;

	private List<IRemoteSenderChannel<K, V>> senders;

	public SuperCache(String name, IPersistenceChannel<K, V> channel) {
		LOGGER.info("Initializing {}", name);
		cache = new TheCache<>(name, channel);

		this.senders = Lists.newArrayList();
	}

	public void registerRemoteSenderChannel(IRemoteSenderChannel<K, V> channel) {
		senders.add(channel);
	}

	public V get(K key) {
		return cache.get(key);
	}

	public void put(K key, V value) {
		this.cache.put(key, value);
		sendToTheWorld(key, value, OPERATION.SAVE);
	}

	public boolean remove(K key) {
		sendToTheWorld(key, null, OPERATION.EVICT);
		return this.cache.evictIfPresent(key);
	}

	void putFromRemote(K key, V value) {
		this.cache.put(key, value);
	}

	void removeFromRemote(K key) {
		this.cache.evictIfPresent(key);
	}

	private void sendToTheWorld(K key, V value, OPERATION op) {
		for (IRemoteSenderChannel<K, V> sender : senders) {
			sender.send(key, value, op);
		}
	}

	public String getName() {
		return this.cache.getName();
	}
}
