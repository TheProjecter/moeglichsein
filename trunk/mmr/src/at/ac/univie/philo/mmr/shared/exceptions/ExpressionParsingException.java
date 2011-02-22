package at.ac.univie.philo.mmr.shared.exceptions;

import java.io.Serializable;

public class ExpressionParsingException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpressionParsingException() {
		super();
	}
	
	public ExpressionParsingException(String msg) {
		super(msg);
	}
}
