package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XorOperator implements IBinaryOperator, IsSerializable {


	public TruthExpression evaluate(TruthExpression left, TruthExpression right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException();
		}
		
		IBinaryOperator bicond = Operators.getBiconditionalOperator();
		TruthExpression bicondLeftRight = bicond.evaluate(left, right);
		if (!bicondLeftRight.getValue().equals(TruthValue.TRUE)) {
			return new TruthExpression(TruthValue.TRUE);
		}
		return new TruthExpression(TruthValue.FALSE);
	}


	public String getName() {
		return "\u2295";
	}


	public int getPriority() {
		return this.XOR_PRIORITY;
	}

	public boolean isAssociative() {
		return true;
	}

}
