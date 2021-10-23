package ch.bemar.supercache.comm.impl;

import java.io.Serializable;

import ch.bemar.supercache.comm.ITransferContainer;

public class TransferContainer<K extends Serializable, V extends Serializable> implements ITransferContainer<K, V> {

	private K key;
	private V value;
	private OPERATION operation;

	private static final long serialVersionUID = -5746567767072478498L;

	public TransferContainer(K key, V value, OPERATION op) {
		super();
		this.key = key;
		this.value = value;
		this.operation = op;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public OPERATION getOperation() {
		return operation;
	}

	@Override
	public String toString() {
		return "TransferContainer [key=" + key + ", value=" + value + ", operation=" + operation + "]";
	}

}
