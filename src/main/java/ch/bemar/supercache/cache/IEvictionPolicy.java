package ch.bemar.supercache.cache;

import java.io.Serializable;

public interface IEvictionPolicy<K extends Serializable, V extends Serializable> {

	public boolean isEvictionCandidate(V object);

}
