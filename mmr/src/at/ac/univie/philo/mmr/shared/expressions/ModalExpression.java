package at.ac.univie.philo.mmr.shared.expressions;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.operators.IModalOperator;

public class ModalExpression extends FormulaExpression implements IsSerializable {

	IModalOperator modalOperator;
	Expression scope;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public ModalExpression() {
		super(new Expression[]{});
	}
	
	public ModalExpression(IModalOperator modalOperator, Expression scope) {
		super(new Expression[]{scope});
		this.modalOperator = modalOperator;
		this.scope = scope;
		valid();
	}

	private void valid() {
		if (modalOperator == null || scope == null) {
			throw new IllegalArgumentException("something is null.");
		}
	}
	
	public IModalOperator getModalOperator() {
		return modalOperator;
	}

	public Expression getScope() {
		return scope;
	}

	@Override
	public String toString() {
		return modalOperator.getName() + "(" + scope.toString() +")";
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if (visitor.preVisit(this)) {
			visitor.visit(this);
		}
	}

	@Override
	public Collection<VariableExpression> allVariables() {
		return scope.allVariables();
	}

	@Override
	public Collection<VariableExpression> freeVariables() {
		return scope.freeVariables();
	}

	@Override
	public Expression replace(TermExpression oldTerm, TermExpression newTerm) {
		return new ModalExpression(modalOperator, scope.replace(oldTerm, newTerm));
	}
}
