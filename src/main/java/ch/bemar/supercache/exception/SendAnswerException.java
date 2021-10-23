package ch.bemar.supercache.exception;

public class SendAnswerException extends Exception {



	/**
	 * 
	 */
	private static final long serialVersionUID = -2462587482044216113L;

	public SendAnswerException() {
		super();
	}

	public SendAnswerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SendAnswerException(String message, Throwable cause) {
		super(message, cause);
	}

	public SendAnswerException(String message) {
		super(message);
	}

	public SendAnswerException(Throwable cause) {
		super(cause);
	}

}
