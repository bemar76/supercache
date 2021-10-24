package ch.bemar.supercache.cache.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bemar.supercache.cache.IEvictionPolicy;
import ch.bemar.supercache.cache.IPersistenceChannel;
import ch.bemar.supercache.exception.CacheException;

public class TheCache<K extends Serializable, V extends Serializable> implements Serializable {

	private Logger LOGGER;

	private static final long serialVersionUID = -7236566513268175100L;

	private IPersistenceChannel<K, V> persistenceChannel;

	private final IEvictionPolicy<K, V> evictionPolicy;

	private final Map<K, V> cache;

	private final String name;

	public TheCache(String name, IPersistenceChannel<K, V> pChannel) {
		this(name, pChannel, null);
	}

	public TheCache(String name, IPersistenceChannel<K, V> pChannel, IEvictionPolicy<K, V> evictionPolicy) {
		LOGGER = LoggerFactory.getLogger(TheCache.class + " | " + name);

		this.name = name;
		this.cache = Collections.synchronizedMap(new HashMap<K, V>());
		this.evictionPolicy = evictionPolicy;

		setPersistanceChannel(pChannel);

		loadAll();

	}

	private void setPersistanceChannel(IPersistenceChannel<K, V> pChannel) {
		if (pChannel != null) {

			this.persistenceChannel = pChannel;
			LOGGER.debug("Setting persistance channel");

		} else {

			this.persistenceChannel = new DummyPersistenceChannel<>();
			LOGGER.debug("Setting persistance channel with dummy channel");
		}
	}

	void loadAll() {

		LOGGER.debug("loadAll");
		cache.putAll(persistenceChannel.loadAll());

	}

	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}

	public V get(K key) {

		if (containsKey(key)) {
			return cache.get(key);
		}

		V value = null;
		try {
			value = persistenceChannel.load(key);
			
			if (value != null)
				put(key, value);

		} catch (CacheException e) {
			LOGGER.error("Error getting key " + key + " from loader", e);
		}

		return value;
	}

	public void put(K key, V value) {
		LOGGER.trace("Putting key '{}' with value '{}' to cache", key, value);
		cache.put(key, value);
		persistenceChannel.put(key, value);
	}

	public boolean putIfAbsent(K key, V value) {

		if (cache.containsKey(key)) {
			return false;
		}

		put(key, value);
		return true;
	}

	public void evict(K key) {
		LOGGER.trace("Removing key '{}' from cache", key);
		cache.remove(key);
		persistenceChannel.remove(key);
	}

	public boolean evictIfPresent(K key) {

		if (!cache.containsKey(key)) {
			return false;
		}

		evict(key);
		return true;
	}

	public void clear() {
		cache.clear();
	}

	public String getName() {
		return name;
	}
}
