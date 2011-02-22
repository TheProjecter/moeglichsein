package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.QuantorExpression;

/**
 * This class returns the objects associated to the four operators defined
 * in the homework statement. Note that multiple calls for one operator
 * should result in the same object being returned.
 */
public final class Operators {
	
	public static IBinaryOperator getAndOperator() {
		return new AndOperator();
	}
	
	public static IBinaryOperator getOrOperator() {
		return new OrOperator();
	}
	
	public static IBinaryOperator getXorOperator() {
		return new XorOperator();
	}
	
	public static IBinaryOperator getImplicationOperator() {
		return new ImplicationOperator();
	}
	public static IBinaryOperator getBiconditionalOperator() {
		return new BiconditionalOperator();
	}
	
	public static NegationOperator getNegationOperator() {
		return new NegationOperator();
	}
	
	public static IQuantor getAllQuantorOperator() {
		return new AllQuantor();
	}
	
	public static IQuantor getExistenceQuantor() {
		return new ExistenceQuantor();
	}
	
	public static IModalOperator getNecessityOperator() {
		return new NecessityOperator();
	}
	
	public static IModalOperator getPossibilityOperator() {
		return new PossibilityOperator();
	}
}
