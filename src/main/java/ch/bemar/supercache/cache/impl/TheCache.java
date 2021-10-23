package ch.bemar.supercache.cache.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bemar.supercache.cache.IChacheLoader;
import ch.bemar.supercache.cache.IEvictionPolicy;
import ch.bemar.supercache.exception.CacheException;

public class TheCache<K extends Serializable, V extends Serializable> implements Serializable {

	private Logger LOGGER;

	private static final long serialVersionUID = -7236566513268175100L;

	private final IChacheLoader<K, V> loader;

	private final IEvictionPolicy<K, V> evictionPolicy;

	private final Map<K, V> cache;

	private final String name;

	public TheCache(String name, IChacheLoader<K, V> loader) {
		this(name, loader, null);
	}

	public TheCache(String name, IChacheLoader<K, V> loader, IEvictionPolicy<K, V> evictionPolicy) {
		this.name = name;
		this.cache = Collections.synchronizedMap(new HashMap<K, V>());
		this.evictionPolicy = evictionPolicy;
		this.loader = loader;
		LOGGER = LoggerFactory.getLogger(TheCache.class + " | " + name);

		if (this.loader != null) {
			loadAll();
		}
	}

	void loadAll() {
		LOGGER.debug("loadAll");
		cache.putAll(loader.loadAll());
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
			value = loader.load(key);
			cache.put(key, value);

		} catch (CacheException e) {
			LOGGER.error("Error getting key " + key + " from loader", e);
		}

		return value;
	}

	public void put(K key, V value) {

		cache.put(key, value);
	}

	public boolean putIfAbsent(K key, V value) {

		if (cache.containsKey(key)) {
			return false;
		}

		put(key, value);
		return true;
	}

	public void evict(K key) {
		cache.remove(key);
	}

	public boolean evictIfPresent(K key) {

		if (!cache.containsKey(key)) {
			return false;
		}

		cache.remove(key);
		return true;
	}

	public void clear() {

		cache.clear();
	}

	public String getName() {
		return name;
	}
}
