package at.ac.univie.philo.mmr.shared.exceptions;

public class EvaluationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EvaluationException(String msg) {
		super(msg);
	}
	
	public EvaluationException(Exception e) {
		super(e);
	}
}
