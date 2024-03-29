package at.ac.univie.philo.mmr.shared.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VariableExpression extends TermExpression implements IsSerializable {

	HashSet<VariableExpression> vars;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public VariableExpression() {
		super(new Symbol("dummysymbol",1));
	}
	
	public VariableExpression(Variable symbol) {
		super(symbol);
		vars = new HashSet<VariableExpression>();
		vars.add(this);
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if(visitor.preVisit(this)) {
			visitor.visit(this);
		}	
	}

	@Override
	public Collection<VariableExpression> allVariables() {
		return vars;
	}

	@Override
	public Collection<VariableExpression> freeVariables() {
		return vars;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof VariableExpression) {
			VariableExpression var = (VariableExpression) o;
			boolean result = ((Variable) this.getSymbol()).equals(((Variable)var.getSymbol()));
			return result;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getSymbol().hashCode();
	}

	@Override
	public TermExpression replace(TermExpression oldTerm, TermExpression newTerm) {
		if (this.equals(oldTerm)) {
			return newTerm;
		}
		return this;
	}
}
