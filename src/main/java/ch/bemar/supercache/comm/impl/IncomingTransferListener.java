package ch.bemar.supercache.comm.impl;

import java.io.Serializable;

import ch.bemar.supercache.cache.IRemoteReceiverChannel;
import ch.bemar.supercache.comm.ITransferContainer;
import ch.bemar.supercache.comm.ITransferContainer.OPERATION;

public class IncomingTransferListener<K extends Serializable, V extends Serializable> {

	private final IRemoteReceiverChannel<K, V> receiver;

	public IncomingTransferListener(IRemoteReceiverChannel<K, V> receiver) {
		super();
		this.receiver = receiver;
	}

	public void addIncomingTransfer(ITransferContainer<K, V> transfer) {

		if (OPERATION.SAVE.equals(transfer.getOperation())) {

			receiver.put(transfer.getKey(), transfer.getValue());

		} else if (OPERATION.EVICT.equals(transfer.getOperation())) {

			receiver.remove(transfer.getKey());

		} else {
			
			throw new UnsupportedOperationException("The Operation " + transfer.getOperation() + " is not implemented");
		}

	}

}
