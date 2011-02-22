package at.ac.univie.philo.mmr.shared.expressions;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.operators.NegationOperator;

public class NegationExpression extends FormulaExpression implements IsSerializable {

	Expression position;
	NegationOperator operator;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public NegationExpression() {
		super(new Expression[]{});
	}
	
	public NegationExpression(NegationOperator negationOperator, FormulaExpression position) {
		super(new Expression[]{position});
		this.position = position;
		this.operator = negationOperator;
		valid();
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if (visitor.preVisit(this)) {
			position.accept(visitor);
			visitor.visit(this);
		}

	}
	
	@Override
	public String toString() {
		return operator.toString() + "(" + position.toString() + ")";
	}
	
	private void valid() {
		if (position == null || !(position instanceof FormulaExpression) || operator == null) {
			throw new IllegalArgumentException("Input check failed because something is null.");
		}
	}
	
	public NegationOperator getOperator() {
		return operator;
		
	}

	@Override
	public Collection<VariableExpression> allVariables() {
		return position.allVariables();
	}

	@Override
	public Collection<VariableExpression> freeVariables() {
		return position.freeVariables();
	}

	@Override
	public Expression replace(TermExpression oldTerm, TermExpression newTerm) {
		return new NegationExpression(operator, (FormulaExpression) position.replace(oldTerm, newTerm));
	}

}
