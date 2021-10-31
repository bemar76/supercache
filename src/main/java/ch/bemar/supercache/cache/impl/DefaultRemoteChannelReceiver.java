package ch.bemar.supercache.cache.impl;

import java.io.Serializable;

import ch.bemar.supercache.comm.IRemoteReceiverChannel;

public class DefaultRemoteChannelReceiver<K extends Serializable, V extends Serializable>
		implements IRemoteReceiverChannel<K, V> {

	private SuperCache<K, V> superCache;

	@Override
	public void put(K key, V value) {
		superCache.putFromRemote(key, value);
	}

	public void registerCache(SuperCache<K, V> cache) {
		this.superCache = cache;

	}

	@Override
	public void remove(K key) {
		this.superCache.removeFromRemote(key);
	}

}
