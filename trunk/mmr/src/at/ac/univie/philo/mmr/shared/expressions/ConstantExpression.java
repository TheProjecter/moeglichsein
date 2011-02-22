package at.ac.univie.philo.mmr.shared.expressions;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

import at.ac.univie.philo.mmr.shared.semantic.Individual;

public class ConstantExpression extends TermExpression implements IsSerializable {

	private static final ArrayList<VariableExpression> vars = new ArrayList<VariableExpression>();
	private static final ArrayList<VariableExpression> freevars = new ArrayList<VariableExpression>();
	private Individual individual = null;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	**/
	public ConstantExpression() {
		super(new Symbol("dummy", 0));
	}
	
	public ConstantExpression(Constant symbol) {
		super(symbol);
	}
	
	/**
	 * This is a constructor for a temporary Constant, which directly maps to an individual i.
	 * @param i Individual which is temporary hardcoded for this constant in this world
	 */
	public ConstantExpression(Individual i) {
		super(new Symbol(i.getName(), 0));
		if (i != null) {
			this.individual = i;
		}
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if(visitor.preVisit(this)) {
			visitor.visit(this);
		}	
	}
	
	/**
	 * 
	 * @return the assigned individual or null if it is not a temporary constant for quantor evaluation
	 */
	public Individual getIndividual() {
		return individual;
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
	public TermExpression replace(TermExpression oldTerm, TermExpression newTerm) {
		if (this.equals(oldTerm)) {
			return newTerm;
		} else {
			return this;
		}
	}
}
