package at.ac.univie.philo.mmr.shared.expressions;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TruthExpression extends Expression implements IsSerializable {

	private TruthValue truthValue;
	private static final ArrayList<VariableExpression> vars = new ArrayList<VariableExpression>();
	private static final ArrayList<VariableExpression> freevars = new ArrayList<VariableExpression>();
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public TruthExpression() {
		super(new Expression[]{});
		this.truthValue = TruthValue.FALSE;
	}
	
	public TruthExpression(TruthValue truthValue) {
		super(new Expression[]{});
		this.truthValue = truthValue;
		valid();
	}

	public TruthExpression(boolean truthval) {
		super(new Expression[]{});
		if (truthval) {
			this.truthValue = TruthValue.TRUE;
		} else {
			this.truthValue = TruthValue.FALSE;
		}
	}

	public TruthValue getValue() {
		return truthValue;
	}
	
	private void valid() {
		if (truthValue == null) {
			throw new IllegalArgumentException("TruthValue is null");
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TruthExpression) {
			TruthExpression other = (TruthExpression)o;
			return (other.getValue()).equals(this.getValue());
		}
		return false;
	}

	@Override
	public void accept(IExpressionVisitor visitor) {
		if (visitor.preVisit(this)) {
			visitor.visit(this);
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
	public String toString() {
		if (truthValue.equals(TruthValue.TRUE)) {
			return "\\top";
		} else if (truthValue.equals(TruthValue.FALSE)) {
			return "\\bot";
		} else {
			return truthValue.toString();
		}
	}

	@Override
	public Expression replace(TermExpression oldTerm, TermExpression newTerm) {
		return this;
	}
	
}
