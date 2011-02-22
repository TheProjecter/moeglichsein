package at.ac.univie.philo.mmr.shared.expressions;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FunctionExpression extends TermExpression implements IsSerializable {

	TermExpression[] terms;
	Function symbol;
	ArrayList<VariableExpression> vars;
	ArrayList<VariableExpression> freevars;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public FunctionExpression() {
		super(new Symbol("dummyfunction",1));
	}
	
	public FunctionExpression(Function symbol, TermExpression ...expressions ) {
		super(symbol);
		this.symbol = symbol;
		this.terms = expressions;
		valid();
		vars = new ArrayList<VariableExpression>();
		freevars = new ArrayList<VariableExpression>();
		for(TermExpression expression : expressions) {
			vars.addAll(expression.allVariables());
			freevars.addAll(expression.freeVariables());
		}
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(symbol.toString());
		result.append("(");
		for (int i=0; i < terms.length; i++) {
			if (i!= 0) {
				result.append(", ");
			}
			result.append(terms[i].toString());
		}
		result.append(")");
		return result.toString();	
	}
	
	@Override
	public void accept(IExpressionVisitor visitor) {
		if (visitor.preVisit(this)) {
			//we need to examine every term before we can visit the function expression itself
			for(TermExpression term : terms) {
				term.accept(visitor);
			}
			visitor.visit(this);
		}
	}
	
	private void valid() {
		if (terms == null || symbol == null) {
			throw new IllegalArgumentException("No given terms or null-symbol.");
		}
		
		if (terms.length != symbol.getArity()) {
			throw new IllegalArgumentException("Symbol has a different Arity from the given Number of Terms");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionExpression) {
			FunctionExpression otherFe = (FunctionExpression)o;
			if (!symbol.equals(otherFe.getSymbol())) {
				return false;
			}
			if (this.terms.length != otherFe.terms.length) {
				return false;
			}
			for (int i =0; i< terms.length; i++) {
				if (!otherFe.terms[i].equals(this.terms[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
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
	public FunctionExpression replace(TermExpression oldTerm,
			TermExpression newTerm) {
		TermExpression newTerms[] = new TermExpression[terms.length];
		for (int i=0; i<terms.length; i++) {
			newTerms[i] = (TermExpression) terms[i].replace(oldTerm, newTerm);
		}
		return new FunctionExpression(symbol, newTerms);
	}

}
