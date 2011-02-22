package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;

public interface IBinaryOperator {
	
	public static final int BICONDITIONAL_PRIORITY = 1;
	public static final int IMPLICATION_PRIORITY = 2;
	public static final int DISJUNCTION_PRIORITY = 3;
	public static final int CONJUNCTION_PRIORITY = 4;
	public static final int XOR_PRIORITY = 5;
	
	/**
	 * Returns the name of the operator, as it is used for printing purposes.
	 */
	String getName();
	
	/**
	 * Returns a number corresponding to the operator priority. Two operators on
	 * the same priority level should have the same priority number.
	 */
	int getPriority();
	
	/**
	 * Returns whether the operator is associative or not.
	 */
	boolean isAssociative();
	
	TruthExpression evaluate(TruthExpression left, TruthExpression right);
}
