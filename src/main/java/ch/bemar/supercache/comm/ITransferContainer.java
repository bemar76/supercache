package ch.bemar.supercache.comm;

import java.io.Serializable;

public interface ITransferContainer<K extends Serializable, V extends Serializable> extends Serializable {

	public enum OPERATION {
		SAVE, EVICT
	}

	public K getKey();

	public V getValue();

	public OPERATION getOperation();

}
