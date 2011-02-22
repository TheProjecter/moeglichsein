package at.ac.univie.philo.mmr.shared.operators;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

public class AndOperator implements IBinaryOperator,IsSerializable {

	@Override
	public TruthExpression evaluate(TruthExpression left, TruthExpression right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException();
		}
		
		if (left.getValue().equals(TruthValue.TRUE) && right.getValue().equals(TruthValue.TRUE)) {
			return new TruthExpression(TruthValue.TRUE);
		}
		return new TruthExpression(TruthValue.FALSE);
	}

	@Override
	public String getName() {
		return "AND";
	}

	@Override
	public int getPriority() {
		return this.CONJUNCTION_PRIORITY;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

}
