package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class FormulaExpression extends Expression implements IsSerializable {

	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public FormulaExpression() {
		super();
	}
	
	public FormulaExpression(Expression[] operands) {
		super(operands);
	}
}
