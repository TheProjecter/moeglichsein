package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImplicationOperator implements IBinaryOperator, IsSerializable {

	@Override
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

	@Override
	public String getName() {
		return "\u2192";
	}

	@Override
	public int getPriority() {
		return this.IMPLICATION_PRIORITY;
	}

	@Override
	public boolean isAssociative() {
		return false;
	}

}
