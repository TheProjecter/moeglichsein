package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImplicationOperator implements IBinaryOperator, IsSerializable {

	public TruthExpression evaluate(TruthExpression left, TruthExpression right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException();
		}
		
		TruthExpression negLeft = Operators.getNegationOperator().evaluate(left);
		if (negLeft.getValue().equals(TruthValue.TRUE) || right.getValue().equals(TruthValue.TRUE)) {
			return new TruthExpression(TruthValue.TRUE);
		}
		return new TruthExpression(TruthValue.FALSE);
	}

	public String getName() {
		return "\u2192";
	}

	public int getPriority() {
		return this.IMPLICATION_PRIORITY;
	}

	public boolean isAssociative() {
		return false;
	}

}
