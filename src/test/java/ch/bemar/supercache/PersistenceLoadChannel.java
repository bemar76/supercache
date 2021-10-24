package ch.bemar.supercache;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import ch.bemar.supercache.cache.IPersistenceChannel;

public class PersistenceLoadChannel<K extends Serializable, V extends Serializable>
		implements IPersistenceChannel<K, V> {

	private Map<K, V> database = Maps.newConcurrentMap();

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceLoadChannel.class);

	public PersistenceLoadChannel() {

	}

	public void preload(Map<K, V> database) {
		LOGGER.trace("preloading values {}", database);
		this.database.putAll(database);
	}

	public V get(K key) {
		LOGGER.trace("getting value with key {}", key);
		return this.database.get(key);
	}

	public void put(K key, V value) {
		LOGGER.trace("putting key {} and value {} to db", key, value);
		this.database.put(key, value);
	}

	@Override
	public V load(K key) {
		return get(key);
	}

	@Override
	public Map<K, V> loadAll() {
		LOGGER.debug("return complete map in loadAll method");
		return database;
	}

	@Override
	public V remove(K key) {
		LOGGER.trace("removing value with key {}", key);
		return database.remove(key);
	}
}
