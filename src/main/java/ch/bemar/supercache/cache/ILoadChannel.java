package ch.bemar.supercache.cache;

import java.io.Serializable;

public interface ILoadChannel<K extends Serializable, V extends Serializable> extends IChacheLoader<K, V> {

}
