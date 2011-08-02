package at.ac.univie.philo.mmr.shared.operators;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

public class AndOperator implements IBinaryOperator,IsSerializable {

	public TruthExpression evaluate(TruthExpression left, TruthExpression right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException();
		}
		
		if (left.getValue().equals(TruthValue.TRUE) && right.getValue().equals(TruthValue.TRUE)) {
			return new TruthExpression(TruthValue.TRUE);
		}
		return new TruthExpression(TruthValue.FALSE);
	}

	public String getName() {
		return "\u2227";
	}

	public int getPriority() {
		return this.CONJUNCTION_PRIORITY;
	}

	public boolean isAssociative() {
		return true;
	}

}
