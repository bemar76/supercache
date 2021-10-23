package ch.bemar.supercache.cache;

import java.io.Serializable;
import java.util.Map;

import ch.bemar.supercache.exception.CacheException;

public interface IChacheLoader<K extends Serializable, V extends Serializable> {

	public V load(K key) throws CacheException;

	public Map<K, V> loadAll();

}
