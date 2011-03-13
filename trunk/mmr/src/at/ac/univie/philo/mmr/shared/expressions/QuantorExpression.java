package at.ac.univie.philo.mmr.shared.expressions;

import java.util.Collection;
import java.util.HashSet;

import at.ac.univie.philo.mmr.shared.operators.IQuantor;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QuantorExpression extends FormulaExpression implements IsSerializable {

	Expression scope;
	Variable boundedVar;
	IQuantor quantor;
	HashSet<VariableExpression> vars;
	HashSet<VariableExpression> freevars;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public QuantorExpression() {
		super(new Expression[]{});
	}
	
	public QuantorExpression(IQuantor quantor, Variable boundedVar, Expression scope) {
		super(new Expression[]{scope});
		this.boundedVar = boundedVar;
		this.scope = scope;
		this.quantor = quantor;
		valid();
		vars = new HashSet<VariableExpression>();
		freevars = new HashSet<VariableExpression>();
		vars.addAll(scope.allVariables());
		freevars.addAll(scope.freeVariables());
		freevars.remove(new VariableExpression(boundedVar));
		freevars.contains(null);
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if(visitor.preVisit(this)) {
			visitor.visit(this);
		}

	}
	
	private void valid() {
		if (boundedVar == null || scope == null || quantor == null) {
			throw new IllegalArgumentException("Some null argument given.");
		}
	}
	
	public IQuantor getQuantor()  {
		return quantor;
	}

	public Expression getScope() {
		return scope;
	}

	public Variable getBoundedVar() {
		return boundedVar;
	}

	@Override
	public Collection<VariableExpression> allVariables() {
		return vars;
	}

	@Override
	public Collection<VariableExpression> freeVariables() {
		return freevars;
	}

	@Override
	public String toString() {
		return quantor.getName()+" "+boundedVar.toString()+"("+scope.toString()+")"; 
	}

	@Override
	public Expression replace(TermExpression oldTerm, TermExpression newTerm) {
		return new QuantorExpression(quantor, boundedVar, scope.replace(oldTerm, newTerm));
	}
	
}
