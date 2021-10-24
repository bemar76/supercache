package ch.bemar.supercache.cache;

import java.io.Serializable;

public interface IPersistenceChannel<K extends Serializable, V extends Serializable> extends IChacheLoader<K, V> {

	

}
