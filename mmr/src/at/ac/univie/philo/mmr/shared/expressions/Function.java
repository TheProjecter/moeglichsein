package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Function extends Symbol implements IsSerializable{

	private int arity;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Function() {
		super("dummyfunction",1);
	}
	
	public Function(String name, int index, int arity) {
		super(name, index);
		this.arity = arity;
		valid();
	}
	
	public int getArity() {
		return arity;
	}

	private void valid() {
		if (arity <= 0) {
			throw new IllegalArgumentException("Arity must be greater 0");
		}
	}
}
