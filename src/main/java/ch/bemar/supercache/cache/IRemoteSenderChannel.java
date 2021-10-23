package ch.bemar.supercache.cache;

import java.io.Serializable;

import ch.bemar.supercache.comm.ITransferContainer.OPERATION;

public interface IRemoteSenderChannel<K extends Serializable, V extends Serializable> {
	
	public void send(K key, V value, OPERATION op);

}
