package ch.bemar.supercache.cache;

import java.io.Serializable;

import ch.bemar.supercache.cache.impl.SuperCache;

public interface IRemoteReceiverChannel<K extends Serializable, V extends Serializable> {

	public void registerCache(SuperCache<K, V> cache);

	public void put(K key, V value);
	
	public void remove(K key);

}
