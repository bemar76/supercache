package ch.bemar.supercache.exception;

public class CacheException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -16639764119616859L;

	public CacheException() {
		super();
	}

	public CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheException(String message) {
		super(message);
	}

	public CacheException(Throwable cause) {
		super(cause);
	}

}
