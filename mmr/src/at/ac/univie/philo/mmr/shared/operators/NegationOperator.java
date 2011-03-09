package at.ac.univie.philo.mmr.shared.operators;

import at.ac.univie.philo.mmr.shared.expressions.TruthExpression;
import at.ac.univie.philo.mmr.shared.expressions.TruthValue;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NegationOperator implements IsSerializable {

	public TruthExpression evaluate(TruthExpression position) {
		if (position != null) {
			if (position.getValue().equals(TruthValue.TRUE)) {
				return new TruthExpression(TruthValue.FALSE);
			} else {
				return new TruthExpression(TruthValue.TRUE);
			}
		} 	else {
			throw new IllegalArgumentException("position is null");
		}
	}
	
	public String toString() {
		return "\u00AC";
	}
}
