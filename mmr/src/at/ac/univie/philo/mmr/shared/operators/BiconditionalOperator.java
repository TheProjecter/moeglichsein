package at.ac.univie.philo.mmr.shared.operators;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

public class BiconditionalOperator implements IBinaryOperator, IsSerializable {

	public TruthExpression evaluate(TruthExpression left, TruthExpression right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException();
		}
		
		IBinaryOperator implic = Operators.getImplicationOperator();
		TruthExpression leftImplicRight = implic.evaluate(left, right);
		TruthExpression rightImplicLeft = implic.evaluate(right, left);
		if (leftImplicRight.getValue().equals(TruthValue.TRUE) && rightImplicLeft.getValue().equals(TruthValue.TRUE)) {
			return new TruthExpression(TruthValue.TRUE);
		}
		return new TruthExpression(TruthValue.FALSE);
	}

	
	public String getName() {
		return "\u2194";
	}

	
	public int getPriority() {
		return this.BICONDITIONAL_PRIORITY;
	}

	
	public boolean isAssociative() {
		return false;
	}

}
