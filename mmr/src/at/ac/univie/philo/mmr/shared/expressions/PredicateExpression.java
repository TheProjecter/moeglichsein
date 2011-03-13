package at.ac.univie.philo.mmr.shared.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PredicateExpression extends FormulaExpression implements IsSerializable {

	TermExpression[] terms;
	Predicate symbol;

	Collection<VariableExpression> vars;
	Collection<VariableExpression> freevars;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public PredicateExpression() {
		super(new Expression[]{});
	}
	
	public PredicateExpression(Predicate symbol, TermExpression ...terms ) {
		super(terms);
		this.terms = terms;
		this.symbol = symbol;
		valid();
		vars = new HashSet<VariableExpression>();
		freevars = new HashSet<VariableExpression>();
		for(TermExpression expression : terms) {
			vars.addAll(expression.allVariables());
			freevars.addAll(expression.freeVariables());
		}
	}

	public TermExpression[] getTerms() {
		return terms;
	}
	
	public Predicate getSymbol() {
		return symbol;
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
			throw new IllegalArgumentException("No given terms or symbol.");
		}
		
		if (terms.length != symbol.getArity()) {
			throw new IllegalArgumentException("Symbol has a different Arity from the given Number of Terms");
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
	
	public ArrayList<Integer> getfreeVariableIndices() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i=0; i<terms.length; i++) {
			if(terms[i] instanceof VariableExpression) {
				res.add(i);
			}
		}
		return res;
	}
	
	@Override
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append(symbol.toString() + "(");
		for (int i = 0; i<terms.length; i++) {
			if (i != 0) {
				strB.append(", ");
			}
			strB.append(terms[i].toString());
		}
		strB.append(")");
		return strB.toString();
	}
	
	/**
	 * Replaces every occurrence of one term with another term. This is a helper-method for evaluating terms.
	 * @param oldTerm
	 * @param newTerm
	 * @return
	 */
	@Override
	public PredicateExpression replace(TermExpression oldTerm, TermExpression newTerm) {
		TermExpression newTerms[] = new TermExpression[terms.length];
		for (int i=0; i<terms.length; i++) {
			newTerms[i] = (TermExpression) terms[i].replace(oldTerm, newTerm);
		}
		return new PredicateExpression(symbol, newTerms);
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PredicateExpression) {
			PredicateExpression otherPe = (PredicateExpression)o;
			if (!symbol.equals(otherPe.symbol)) {
				return false;
			}
			if (this.terms.length != otherPe.terms.length) {
				return false;
			}
			for (int i =0; i< terms.length; i++) {
				if (!otherPe.terms[i].equals(this.terms[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public PredicateExpression myClone() {
		TermExpression[] termscopy = new TermExpression[terms.length];
		Predicate symbolCopy = new Predicate(symbol.getName(), symbol.getIndex(), symbol.getArity());
		System.arraycopy(this.terms, 0, termscopy, 0, terms.length);
		return new PredicateExpression(symbolCopy, termscopy);
	}

}
