package at.ac.univie.philo.mmr.shared.expressions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Predicate extends Symbol implements IsSerializable {

	private int arity;
	
	/**
	 * Dummy Constructor for GWT Serialization. Don't use it yourself!
	 */
	public Predicate() {
		super("dummypred",1);
	}
	
	public Predicate(String name, int index, int arity) {
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
	
	public boolean equals(Object o) {
		if (o instanceof Predicate) {
			Predicate p = (Predicate)o;
			return (name.equals(p.name) && index == p.index && arity == p.arity);
		}
		return false;
	}
	
}
