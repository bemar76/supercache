package ch.bemar.supercache;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

import ch.bemar.supercache.cache.ILoadChannel;

public class PersistenceLoadChannel<K extends Serializable, V extends Serializable> implements ILoadChannel<K, V> {

	private Map<K, V> database = Maps.newHashMap();

	public PersistenceLoadChannel() {

	}

	public void preload(Map<K, V> database) {
		this.database.putAll(database);
	}

	public V get(K key) {
		return this.database.get(key);
	}

	public void put(K key, V value) {
		this.database.put(key, value);
	}

	public Map getMap() {
		return this.database;
	}

	@Override
	public V load(K key) {
		return get(key);
	}

	@Override
	public Map<K, V> loadAll() {
		return database;
	}
}
