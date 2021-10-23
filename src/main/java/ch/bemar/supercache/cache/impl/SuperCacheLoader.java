package ch.bemar.supercache.cache.impl;

import java.io.Serializable;
import java.util.Map;

import ch.bemar.supercache.cache.IChacheLoader;
import ch.bemar.supercache.cache.ILoadChannel;
import ch.bemar.supercache.exception.CacheException;

public class SuperCacheLoader<K extends Serializable, V extends Serializable> implements IChacheLoader<K, V> {

	private final ILoadChannel<K, V> channel;

	public SuperCacheLoader(ILoadChannel<K, V> channel) {
		super();
		this.channel = channel;
	}

	@Override
	public V load(K key) throws CacheException {
		return channel.load(key);
	}

	@Override
	public Map<K, V> loadAll() {
		return channel.loadAll();
	}

}
