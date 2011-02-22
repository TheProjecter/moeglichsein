package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class TermExpression extends Expression implements IsSerializable  {

	private Symbol symbol;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public TermExpression() {
		super(new Expression[]{});
	}
	
	public TermExpression(Symbol symbol) {
		super(new Expression[]{});
		this.symbol = symbol;
		valid();
	}
	
	public Symbol getSymbol() {
		return symbol;
	}
	
	private void valid() {
		if (symbol == null) {
			throw new IllegalArgumentException("Symbol is null");
		}
	}
	public boolean equals(Object o) {
		if (o instanceof TermExpression) {
			return symbol.equals(((TermExpression)o).getSymbol());
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.getSymbol().toString();
	}

}
