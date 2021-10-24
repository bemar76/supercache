package ch.bemar.supercache.cache.impl;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

import ch.bemar.supercache.cache.IPersistenceChannel;
import ch.bemar.supercache.exception.CacheException;

/**
 * This class is a "placeholder" for a connection to the persistence layer
 * 
 * @author bemar
 *
 * @param <K>
 * @param <V>
 */
public class DummyPersistenceChannel<K extends Serializable, V extends Serializable>
		implements IPersistenceChannel<K, V> {

	@Override
	public V load(K key) throws CacheException {
		return null;
	}

	@Override
	public Map<K, V> loadAll() {
		return Maps.newHashMap();
	}

	@Override
	public void put(K key, V value) {

	}

	@Override
	public V remove(K key) {
		return null;
	}

}
